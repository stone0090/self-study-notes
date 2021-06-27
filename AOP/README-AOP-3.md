> 本文约 2500 字，读完大概需要 10 分钟。
> 本文示例代码可参见：[https://github.com/stone0090/aop-dig-down/proxy-pattern-sample](https://github.com/stone0090/aop-dig-down/tree/master/proxy-pattern-sample)
> 
> - [《AOP 挖掘记》概念介绍及原理初探（一）](https://github.com/stone0090/aop-dig-down/blob/master/README.MD)
> - [《AOP 挖掘记》代理模式、jdk代理、cglib（二）](https://github.com/stone0090/aop-dig-down/blob/master/README-AOP-2.md)
> - [《AOP 挖掘记》jdk代理 - 源码解析（三）](https://github.com/stone0090/aop-dig-down/blob/master/README-AOP-3.md)
> - [《AOP 挖掘记》cglib - 源码解析（四）](https://github.com/stone0090/aop-dig-down/blob/master/README-AOP-4.md)
> - 《AOP 挖掘记》jdk代理、cglib 性能对比（五）【未完成】
> - 《AOP 挖掘记》徒手实现 AOP 框架（六）【未完成】
> - 《AOP 挖掘记》Spring AOP 源码解析（七）【未完成】
> - 《AOP 挖掘记》AspectJ AOP 原理解析（八）【未完成】

前面讲过，**jdk代理** 能够动态生成的代理类的 .class 文件，那么这些 *java字节码* 到底是如何生成的呢？本次我们来一探究竟，深入了解 **jdk代理** 动态生成代理类全过程。

### 时序图

首先，我们看到整个调用链路的时序图，整个过程是从 `Proxy` -> `WeakCache` -> `ProxyClassFactory` -> `ProxyGenerator`，最终生成代理类的 .class 文件的。

![](http://qn.shisb.com/blog/aop/3/1.2.png)

### 源码解析

> tips：以下源码均为 *精简注释版* 源码，如需 *原汁原味版* 源码，请自行 debug。

我们继续看回之前的代码示例。

```java
public class JdkProxyClient {

    public static void main(String[] args) {

        // 输出 jdk代理 动态生成的 .class字节码 到 com.sun.proxy 路径下
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // 创建动态代理类的实例，传入 ClassLoader、共同接口、InvocationHandler接口的实例 3个参数
        Subject proxySubject = (Subject) Proxy.newProxyInstance(
                JdkProxyClient.class.getClassLoader(),
                new Class[] { Subject.class },
                new JdkProxy(new RealSubject()));
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }
}
```

从以上代码中我们得知，代理类的实例 `proxySubject` 是通过方法 `Proxy.newProxyInstance()` 创建出来的，我们首先看 `Proxy.newProxyInstance(...)` 的源码。

```java
@CallerSensitive
public static Object newProxyInstance(
    ClassLoader loader, Class<?>[] interfaces, InvocationHandler h) throws IllegalArgumentException {
    
    // 入参校验，InvocationHandler接口的实例 不允许为 null
    Objects.requireNonNull(h);

    // 拷贝入参接口到局部变量 intfs 中
    final Class<?>[] intfs = interfaces.clone();
    
    // 获取系统安全管理器
    final SecurityManager sm = System.getSecurityManager();

    // 查找或者生成指定的代理类
    Class<?> cl = getProxyClass0(loader, intfs);

    // 利用反射，获取类的构造函数
    final Constructor<?> cons = cl.getConstructor(constructorParams);
    final InvocationHandler ih = h;
    
    // 利用反射，通过构造函数创建代理类的实例
    return cons.newInstance(new Object[]{h});
    
    // ...
}
```

以上代码的核心是：
- `Class<?> cl = getProxyClass0(loader, intfs);` 查找或者生成指定的代理类。
- `final Constructor<?> cons = cl.getConstructor(constructorParams);` 获取代理类的构造函数。
- `return cons.newInstance(new Object[]{h});` 通过构造函数创建代理类的实例。
- 我们继续看 `getProxyClass0(...)` 的源码。

```java
private static Class<?> getProxyClass0(ClassLoader loader, Class<?>... interfaces) {
    // ...
    
    // 假如代理类（入参类加载器+实现+入参接口）已存在，则简单的返回代理类缓存的拷贝，
    // 否则将通过 ProxyClassFactory 创建代理类
    return proxyClassCache.get(loader, interfaces);
}
```

以上代码的核心是注释：`假如代理类（入参类加载器+实现+入参接口）已存在，则简单的返回代理类缓存的拷贝，否则将通过 ProxyClassFactory 创建代理类`，我们继续看 `proxyClassCache.get(...)` 的源码。

```java
final class WeakCache<K, P, V> {
    // ...

    public V get(K key, P parameter) {
        // ...

        while (true) {
            if (supplier != null) {
                // supplier 可能是类 Factory 的实例 或者是 CacheValue<V> 的实例
                V value = supplier.get();
                if (value != null) {
                    return value;
                }
            }
 
            // 懒加载类 Factory 的实例
            if (factory == null) {
                factory = new Factory(key, parameter, subKey, valuesMap);
            }

            if (supplier == null) {
                supplier = valuesMap.putIfAbsent(subKey, factory);
                if (supplier == null) {
                    // successfully installed Factory
                    supplier = factory;
                }
                // else retry with winning supplier
            } else {
                // ...
            }
        }
    }
}
```

以上代码的核心在 `while` 循环里，这段 `while` 循环会被执行两次：
- 第一次循环，懒加载 `factory = new Factory(key, parameter, subKey, valuesMap);`，并且执行 `supplier = factory;` 把 `factory` 赋值给 `supplier`；
- 第二次循环，`supplier` 不再为 `null`，触发 `V value = supplier.get();` 代码块，因为 `supplier = factory;`，所以 `supplier.get();` 等价于 `factory.get()`，我们继续看 `factory.get()` 的源码。

```java
public class Proxy implements java.io.Serializable {
    // ...
    private static final WeakCache<ClassLoader, Class<?>[], Class<?>>
        proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());
}

final class WeakCache<K, P, V> {
    // ...
    public WeakCache(BiFunction<K, P, ?> subKeyFactory,
                     BiFunction<K, P, V> valueFactory) {
        this.subKeyFactory = Objects.requireNonNull(subKeyFactory);
        this.valueFactory = Objects.requireNonNull(valueFactory);
    }
    // ...
    private final class Factory implements Supplier<V> {
        // ...
        @Override
        public synchronized V get() {
            // ...
            // 其中 valueFactory 是 ProxyClassFactory 的实例
            value = Objects.requireNonNull(valueFactory.apply(key, parameter));
            return value;
        }
    }
}
```

以上代码的核心在于 `valueFactory.apply(key, parameter)`，结合类 `Proxy` 的静态成员变量 `proxyClassCache` 和类 `WeakCache` 的构造函数得知，`valueFactory` 是 `ProxyClassFactory` 的实例，所以 `valueFactory.apply(...)` 实际上是在调用 `ProxyClassFactory.apply(...);`，我们继续看 `ProxyClassFactory` 的源码。

```java
/**
 * 一个工厂函数，在给定 ClassLoader 和 接口数组 的情况下 生成、定义、返回 代理类。
 */
private static final class ProxyClassFactory
    implements BiFunction<ClassLoader, Class<?>[], Class<?>>
{
    // 所有代理类的前缀名都以 $Proxy 开头
    private static final String proxyClassNamePrefix = "$Proxy";

    // 使用唯一的编号给作为代理类名的一部分，如 $Proxy0,$Proxy1 等
    private static final AtomicLong nextUniqueNumber = new AtomicLong();

    @Override
    public Class<?> apply(ClassLoader loader, Class<?>[] interfaces) {

        Map<Class<?>, Boolean> interfaceSet = new IdentityHashMap<>(interfaces.length);
        
        // 循环接口数组进行校验
        for (Class<?> intf : interfaces) {
        
            Class<?> interfaceClass = null;
            try {
                interfaceClass = Class.forName(intf.getName(), false, loader);
            } catch (ClassNotFoundException e) {
            }
            
            // 验证传入的接口是否由传入的 ClassLoader 加载的
            if (interfaceClass != intf) {
                throw new IllegalArgumentException(
                    intf + " is not visible from class loader");
            }
            
            // 验证 Class 对象是否为接口。
            if (!interfaceClass.isInterface()) {
                throw new IllegalArgumentException(
                    interfaceClass.getName() + " is not an interface");
            }
            
            // 验证此接口是否重复。
            if (interfaceSet.put(interfaceClass, Boolean.TRUE) != null) {
                throw new IllegalArgumentException(
                    "repeated interface: " + interfaceClass.getName());
            }
        }

        // 声明代理类所在包
        String proxyPkg = null;
        int accessFlags = Modifier.PUBLIC | Modifier.FINAL;

        // 验证你传入的接口中是否有非 public 接口，只要有一个接口是非 public 的，那么这些接口都必须在同一包中
        for (Class<?> intf : interfaces) {
            int flags = intf.getModifiers();
            if (!Modifier.isPublic(flags)) {
                accessFlags = Modifier.FINAL;
                String name = intf.getName();
                int n = name.lastIndexOf('.');
                String pkg = ((n == -1) ? "" : name.substring(0, n + 1));
                if (proxyPkg == null) {
                    proxyPkg = pkg;
                } else if (!pkg.equals(proxyPkg)) {
                    throw new IllegalArgumentException(
                        "non-public interfaces from different packages");
                }
            }
        }

        // 如果都是 public 接口，那么生成的代理类就在 com.sun.proxy 包下
        if (proxyPkg == null) {
            proxyPkg = ReflectUtil.PROXY_PACKAGE + ".";
        }

        // 将当前 nextUniqueNumber 的值以原子的方式的加1，所以第一次生成代理类的名字为 $Proxy0.class
        long num = nextUniqueNumber.getAndIncrement();
        // 代理类的完全限定名，如 com.sun.proxy.$Proxy0.calss
        String proxyName = proxyPkg + proxyClassNamePrefix + num;

        // 生成指定代理类的字节码
        byte[] proxyClassFile = ProxyGenerator.generateProxyClass(
            proxyName, interfaces, accessFlags);
        try {
            // 调用 native 方法，传入字节码，生成 Class 对象
            return defineClass0(loader, proxyName, proxyClassFile, 0, proxyClassFile.length);
        } catch (ClassFormatError e) {
            throw new IllegalArgumentException(e.toString());
        }
    }
}
```

以上代码的核心是：
- 验证传入的接口是否由传入的 ClassLoader 加载的、验证 Class 对象是否为接口、验证此接口是否重复。
- 拼装代理类包名和类名，`ProxyGenerator.generateProxyClass(proxyName, interfaces, accessFlags)` 生成 .class 文件的字节码。
- 调用 native 方法 `defineClass0`，传入字节码，生成 Class 对象。
- 我们继续看 `ProxyGenerator.generateProxyClass(...);` 的源码，了解字节码的生成过程。

> 由于 idea 反编译出 `sun.misc.ProxyGenerator` 的源码晦涩难懂，故以下源码从 [openjdk-7-ProxyGenerator](http://www.docjar.com/html/api/sun/misc/ProxyGenerator.java.html) 中获取。

```java
public class ProxyGenerator {
    private static final boolean saveGeneratedFiles = 
        ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.misc.ProxyGenerator.saveGeneratedFiles"))).booleanValue();
    
    public static byte[] generateProxyClass(final String className, Class<?>[] interfaces, int accessFlags) {
        ProxyGenerator gen = new ProxyGenerator(className, interfaces, accessFlags);
        // 生成字节码
        final byte[] classFile = gen.generateClassFile();
        if (saveGeneratedFiles) {
            // ...
            FileOutputStream file = new FileOutputStream(dotToSlash(name) + ".class");
            file.write(classFile);
            // ...
        }
        return classFile;
    }
    
    // ...
}
```

以上代码的核心是：
- 解释了，为什么在创建代理对象创建之前执行 `System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");` 代码块，会输出 jdk代理 动态生成的 .class字节码 到 com.sun.proxy 路径下，这是因为当 `saveGeneratedFiles = true` 时有相关的代码逻辑。
- 我们继续看 `gen.generateClassFile()` 的源码，了解字节码的生成细节。

```java
/**
 * 为代理类生成一个 .class 文件，此方法驱动类文件生成过程
 */
private byte[] generateClassFile() {

    // ============================================================
    // 步骤1：为所有方法组装 ProxyMethod 对象以生成代理调度代码

    // 记录 java.lang.Object 的 hashCode，equals 和 toString 方法需要代理方法
    // 这是在代理接口的方法之前完成的，因此 java.lang.Object 中的方法优先于代理接口中的重复方法
    // 
    addProxyMethod(hashCodeMethod, Object.class); // hashCodeMethod = Object.class.getMethod("hashCode");
    addProxyMethod(equalsMethod, Object.class); // equalsMethod = Object.class.getMethod("equals", Object.class);
    addProxyMethod(toStringMethod, Object.class); // toStringMethod = Object.class.getMethod("toString");

    // 获得所有接口中的所有方法，并将方法添加到代理方法中
    for (int i = 0; i < interfaces.length; i++) {
        Method[] methods = interfaces[i].getMethods();
        for (int j = 0; j < methods.length; j++) {
            addProxyMethod(methods[j], interfaces[i]);
        }
    }

    // 验证具有相同方法签名的的方法的返回值类型是否一致，因为不可能有两个方法名相同,参数相同，而返回值却不同的方法
    for (List<ProxyMethod> sigmethods : proxyMethods.values()) {
        checkReturnTypes(sigmethods);
    }

    // ============================================================
    // 步骤2：为我们正在生成的类中的所有字段和方法组装 FieldInfo 和 MethodInfo 结构
    try {
        methods.add(generateConstructor());

        for (List<ProxyMethod> sigmethods : proxyMethods.values()) {
            for (ProxyMethod pm : sigmethods) {

                // 将代理字段声明为 Method，10 为 ACC_PRIVATE 和 ACC_STATAIC 的与运算
                // 所以代理类的字段都是 private static Method XXX
                fields.add(new FieldInfo(pm.methodFieldName,
                        "Ljava/lang/reflect/Method;",
                        ACC_PRIVATE | ACC_STATIC));

                // 生成代理类的代理方法
                methods.add(pm.generateMethod());
            }
        }

        // 为代理类生成静态代码块，对一些字段进行初始化
        methods.add(generateStaticInitializer());
    } catch (IOException e) {
        throw new InternalError("unexpected I/O Exception");
    }

    // 代理方法超过65535将抛出异常
    if (methods.size() > 65535) {
        throw new IllegalArgumentException("method limit exceeded");
    }
    // 代理类的字段超过65535将抛出异常
    if (fields.size() > 65535) {
        throw new IllegalArgumentException("field limit exceeded");
    }

    // ============================================================
    // 步骤3：组装出最终的 .class 文件

    // 在开始编写最终类文件之前，请确保为以下项保留常量池索引
    cp.getClass(dotToSlash(className));
    cp.getClass(superclassName);
    for (int i = 0; i < interfaces.length; i++) {
        cp.getClass(dotToSlash(interfaces[i].getName()));
    }

    // 因为我们要编写最终的常量池表，所以不允许在此之后添加新的常量池
    cp.setReadOnly();

    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    DataOutputStream dout = new DataOutputStream(bout);

    try {
        // 把前面准备的信息全部写入到 .class 结构中

        // u4 magic;
        dout.writeInt(0xCAFEBABE);
        // u2 minor_version;
        dout.writeShort(CLASSFILE_MINOR_VERSION);
        // u2 major_version;
        dout.writeShort(CLASSFILE_MAJOR_VERSION);

        cp.write(dout);             // (write constant pool)

        // u2 access_flags;
        dout.writeShort(ACC_PUBLIC | ACC_FINAL | ACC_SUPER);
        // u2 this_class;
        dout.writeShort(cp.getClass(dotToSlash(className)));
        // u2 super_class;
        dout.writeShort(cp.getClass(superclassName));

        // u2 interfaces_count;
        dout.writeShort(interfaces.length);
        // u2 interfaces[interfaces_count];
        for (int i = 0; i < interfaces.length; i++) {
            dout.writeShort(cp.getClass(dotToSlash(interfaces[i].getName())));
        }

        // u2 fields_count;
        dout.writeShort(fields.size());
        // field_info fields[fields_count];
        for (FieldInfo f : fields) {
            f.write(dout);
        }

        // u2 methods_count;
        dout.writeShort(methods.size());
        // method_info methods[methods_count];
        for (MethodInfo m : methods) {
            m.write(dout);
        }

        // u2 attributes_count;
        dout.writeShort(0); // (no ClassFile attributes for proxy classes)
    } catch (IOException e) {
        throw new InternalError("unexpected I/O Exception");
    }

    return bout.toByteArray();
}
```

以上代码的核心是：
- 步骤1：为所有方法组装 ProxyMethod 对象以生成代理调度代码。
- 步骤2：为我们正在生成的类中的所有字段和方法组装 FieldInfo 和 MethodInfo 结构。
- 步骤3：组装出最终的 .class 文件。

为什么以上代码能生成 .class 文件呢？我们来简单的了解下 .class 文件中按照严格的顺序排列的字节流都具体包含些什么数据，如下图所示。代码中输出的内容跟 .class 文件格式是完全匹配的，所以能够顺利生成 .class 文件啦~
![](http://qn.shisb.com/blog/aop/3/2.png)

最后，我们再对照自动生成代理类 `$Proxy0` 的源码，确实能完全匹配函数 `ProxyGenerator.generateClassFile()` 每个步骤。

```java
package com.sun.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import net.sf.cglib.samples.proxypattern.Subject;

public final class $Proxy0 extends Proxy implements Subject {
    private static Method m1;
    private static Method m4;
    private static Method m2;
    private static Method m3;
    private static Method m0;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }

    public final boolean equals(Object var1) throws  {
        try {
            return ((Boolean)super.h.invoke(this, m1, new Object[]{var1})).booleanValue();
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final void doSomething() throws  {
        try {
            super.h.invoke(this, m4, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final String toString() throws  {
        try {
            return (String)super.h.invoke(this, m2, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final void hello(String var1) throws  {
        try {
            super.h.invoke(this, m3, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }

    public final int hashCode() throws  {
        try {
            return ((Integer)super.h.invoke(this, m0, (Object[])null)).intValue();
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    static {
        try {
            m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
            m4 = Class.forName("net.sf.cglib.samples.proxypattern.Subject").getMethod("doSomething");
            m2 = Class.forName("java.lang.Object").getMethod("toString");
            m3 = Class.forName("net.sf.cglib.samples.proxypattern.Subject").getMethod("hello", Class.forName("java.lang.String"));
            m0 = Class.forName("java.lang.Object").getMethod("hashCode");
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```

经过一番源码探索，我们终于明白了 *jdk代理* 是如何自动生成代理类的 .class 文件。在明白其原理之后，我们完全可以自己实现一个动态代理，只是最后一步组装出 .class 文件比较麻烦，可以改生成 .java 文件，然后再动态加载到 JVM 中，应该能够实现同样的效果，下回再跟大家分享。
