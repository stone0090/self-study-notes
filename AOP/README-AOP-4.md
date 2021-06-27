> 本文约 4000 字，读完大概需要 15 分钟。
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

前面讲过，**cglib** 能够动态生成的代理类的 .class 文件，那么这些 *java字节码* 到底是如何生成的呢？本次我们来一探究竟，深入了解 **cglib** 动态生成代理类全过程。

### 时序图

首先，我们看到整个调用链路的时序图，整个过程是从 `Enhancer` -> `AbstractClassGenerator` -> `ClassLoaderData` -> `LoadingCache` -> `ClassLoaderData` -> `AbstractClassGenerator` -> `Enhancer`，最终生成代理类的 .class 文件的，整个过程相比 *jdk代理* 要复杂一些。

![](http://qn.shisb.com/blog/aop/4/1.png)

### 源码解析

> tips：以下源码均为 *精简注释版* 源码，如需 *原汁原味版* 源码，请自行 debug。

我们继续看回之前的代码示例。

```java
public class CglibProxyClient {

    public static void main(String[] args) {

        // 创建一个类生成器 enhancer
        Enhancer enhancer = new Enhancer();
        // 为类生成器 enhancer 指定父类
        enhancer.setSuperclass(RealSubject.class);
        // 为类生成器 enhancer 指定回调函数
        enhancer.setCallback(new CglibProxy());

        // 创建动态代理类的实例
        RealSubject proxySubject = (RealSubject) enhancer.create();
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }
}
```

以上代码的核心是：类 `Enhancer`，首先创建一个类生成器 `enhancer`，然后为类生成器 `enhancer` 指定父类，再为类生成器 `enhancer` 指定回调函数，最后执行 `enhancer.create()` 创建动态代理类的实例 `proxySubject`，所以 `enhancer.create()` 动态创建代理类的入口。我们先看到类 `Enhancer` 的类图。

![](http://qn.shisb.com/blog/aop/4/2.png)

从以上类图得知，类 `Enhancer` 继承抽象类 `AbstractClassGenerator`，并实现接口 `ClassGenerator` 的 `generateClass` 方法，从方法的命名来看，这很有可能就是最终动态生成代理类字节码文件的方法，我们带着这个疑问，先看 `enhancer.create()` 的源码。

```java
/**
 * 生成动态子类以启用方法拦截。
 * 此类开始作为 JDK 1.3 中包含的标准动态代理支持的替代品，但它是基于继承实现 (而不是基于接口实现)。
 * 动态生成的子类重写超类别的非最终方法，并具有回调到用户定义拦截器的能力。
 */
public class Enhancer extends AbstractClassGenerator {
    // ...
    /**
     * 根据需要生成新类, 并使用指定的回调创建新的对象实例。
     * 使用父类的无参构造函数。
     * @return 动态代理类的实例
     */
    public Object create() {
        classOnly = false;
        argumentTypes = null;
        return createHelper();
    }
}
```

以上代码的核心参见注释，我们继续看 `createHelper()` 的源码。

```java
public class Enhancer extends AbstractClassGenerator {
    // ...
    // 静态成员变量 `KEY_FACTORY` 继承抽象类 `KeyFactory` 实现接口 `EnhancerKey` 的方法 `newInstance()`
    // 它是抽象类 `KeyFactory` 动态生成出来的，是一个的数据包装对象
    private static final EnhancerKey KEY_FACTORY =
        (EnhancerKey)KeyFactory.create(EnhancerKey.class, KeyFactory.HASH_ASM_TYPE, null);
    // ...
    private Object createHelper() {
        // 入参校验
        preValidate();
        // 使用入参信息生成 key 值
        Object key = KEY_FACTORY.newInstance((superclass != null) ? superclass.getName() : null,
                ReflectUtils.getNames(interfaces),
                filter == ALL_ZERO ? null : new WeakCacheKey<CallbackFilter>(filter),
                callbackTypes,
                useFactory,
                interceptDuringConstruction,
                serialVersionUID);
        this.currentKey = key;
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 入口在此 ↓↓↓↓↓↓↓↓↓↓↓↓↓
        // 调用父类的 create 方法
        Object result = super.create(key);
        // ===================================
        return result;
    }
    // ...
}
```

以上代码的核心是：
- 静态成员变量 `KEY_FACTORY` 继承抽象类 `KeyFactory` 实现接口 `EnhancerKey` 的方法 `newInstance()`，它是抽象类 `KeyFactory` 动态生成出来的，结构很简单，由7个私有成员变量、2个构造函数、4个方法 `newInstance(),hashCode(),equals(),toString()` 组成，是一个的数据包装对象。.class源文件可参见：[https://github.com/stone0090/aop-dig-down/EnhancerKey](https://github.com/stone0090/aop-dig-down/blob/master/jdkproxy%26cglib/net.sf.cglib.proxy.Enhancer%24EnhancerKey%24%24KeyFactoryByCGLIB%24%247fb24d72.class)。
- 我们继续看 `super.create(key)` 的源码。

```java
abstract public class AbstractClassGenerator<T> implements ClassGenerator {
    // ...
    protected Object create(Object key) {
        // 获取类加载器
        ClassLoader loader = getClassLoader();
        // 获取缓存
        Map<ClassLoader, ClassLoaderData> cache = CACHE;
        // 从缓存中获取对象 ClassLoaderData 的实例
        ClassLoaderData data = cache.get(loader);
        if (data == null) {
            // ...
        }
        this.key = key;
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 入口在此 ↓↓↓↓↓↓↓↓↓↓↓↓↓
        // 从 ClassLoaderData 的实例中获取 动态生成的代理类
        Object obj = data.get(this, getUseCache());
        // ===================================
        // 如果 obj 是 Class，则获取其构造函数实例化出对象
        if (obj instanceof Class) {
            return firstInstance((Class) obj);
        }
        // 如果 obj 是类 EnhancerFactoryData 的实例，同样也是则获取其构造函数实例化出对象
        return nextInstance(obj);
        // ...
    }
    // ...
}
```

以上代码的核心请参见注释，我们继续看 `data.get(this, getUseCache())` 的源码。

```java
abstract public class AbstractClassGenerator<T> implements ClassGenerator {
    // 默认是使用缓存
    private boolean useCache = true;
    // ...
    protected static class ClassLoaderData {
        public Object get(AbstractClassGenerator gen, boolean useCache) {
            // 如果没有使用缓存，则直接生成代理类，如果使用了缓存，则优先从缓存中获取
            if (!useCache) {
              return gen.generate(ClassLoaderData.this);
            } else {
              // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 入口在此 ↓↓↓↓↓↓↓↓↓↓↓↓↓
              Object cachedValue = generatedClasses.get(gen);
              // ===================================
              return gen.unwrapCachedValue(cachedValue);
            }
        }
    }
}
```

以上代码的核心请参见注释，我们继续看 `generatedClasses.get(gen)` 的源码。

```java
public class LoadingCache<K, KK, V> {
    // ...
    public V get(K key) {
        // 如果能从缓存中找到代理类，就直接返回缓存
        final KK cacheKey = keyMapper.apply(key);
        Object v = map.get(cacheKey);
        if (v != null && !(v instanceof FutureTask)) {
            return (V) v;
        }
        // 找不到，则创建动态代理类，并放入缓存中
        return createEntry(key, cacheKey, v);
    }
    
    /**
     * 加载到缓存的项
     */
    protected V createEntry(final K key, KK cacheKey, Object v) {
        FutureTask<V> task;
        boolean creator = false;
        if (v != null) {
            // 另一个线程已加载实例
            task = (FutureTask<V>) v;
        } else {
            // 创建一个可取消的异步任务
            task = new FutureTask<V>(new Callable<V>() {
                public V call() throws Exception {
                    // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 入口在此 ↓↓↓↓↓↓↓↓↓↓↓↓↓
                    return loader.apply(key);
                    // ===================================
                }
            });
            // 假如 map 不存在 cacheKey，则 put cacheKey 和 task 到 map 中
            Object prevTask = map.putIfAbsent(cacheKey, task);
            if (prevTask == null) { 
                // 如果上一次任务为 null，则执行本次任务
                creator = true;
                task.run();
            } else if (prevTask instanceof FutureTask) {
                // 如果上一次任务不为 null，且是 FutureTask 类型，则继续执行上次任务
                task = (FutureTask<V>) prevTask;
            } else {
                // 如果上一次任务不为 null，且不是 FutureTask 类型，则直接当做代理类返回
                return (V) prevTask;
            }
        }

        V result;
        try {
            // 获得异步任务的结果，也就是代理类
            result = task.get();
        } catch (e) {
            // ...
        }
        if (creator) {
            // 将结果放入缓存中
            map.put(cacheKey, result);
        }
        // 返回结果
        return result;
    }
}
```

以上代码的核心请参见注释，我们继续看 `loader.apply(key)` 的源码。

```java
abstract public class AbstractClassGenerator<T> implements ClassGenerator {
    // ...
    protected static class ClassLoaderData {
        // ClassLoaderData 的构造函数
        public ClassLoaderData(ClassLoader classLoader) {
            if (classLoader == null) {
                throw new IllegalArgumentException("classLoader == null is not yet supported");
            }
            this.classLoader = new WeakReference<ClassLoader>(classLoader);
            // 上面的 loader.apply(key) 实际是指向匿名内部类中的方法 apply()
            Function<AbstractClassGenerator, Object> load = 
                    new Function<AbstractClassGenerator, Object>() {
                        public Object apply(AbstractClassGenerator gen) {
                            // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 入口在此 ↓↓↓↓↓↓↓↓↓↓↓↓↓
                            Class klass = gen.generate(ClassLoaderData.this);
                            // ===================================
                            return gen.wrapCachedClass(klass);
                        }
                    };
            generatedClasses = new LoadingCache<AbstractClassGenerator, Object, Object>(GET_KEY, load);
        }
    }
}
```

以上代码的核心请参见注释，我们继续看 `gen.generate(ClassLoaderData.this)` 的源码。

```java
abstract public class AbstractClassGenerator<T> implements ClassGenerator {
    // ...
    protected Class generate(ClassLoaderData data) {
        Class gen;
        Object save = CURRENT.get();
        CURRENT.set(this);
        try {
            // 获取类加载器
            ClassLoader classLoader = data.getClassLoader();
            if (classLoader == null) {
                throw new IllegalStateException("ClassLoader...");
            }
            // 锁住类加载器
            synchronized (classLoader) {
              String name = generateClassName(data.getUniqueNamePredicate());              
              data.reserveName(name);
              this.setClassName(name);
            }
            // 尝试在已经存在的类中获取代理类
            if (attemptLoad) {
                try {
                    gen = classLoader.loadClass(getClassName());
                    return gen;
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
            
            // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 入口在此 ↓↓↓↓↓↓↓↓↓↓↓↓↓
            // 生成代理类字节码
            byte[] b = strategy.generate(this);
            // ===================================

            // ↓↓↓↓↓↓↓↓↓↓↓↓↓ stone 加的，为了输出自动生成的 .class 文件 ↓↓↓↓↓↓↓↓↓↓↓↓↓
            FileOutputStream fos = new FileOutputStream(getClassName() + ".class");
            fos.write(b);
            fos.close();
            // =================================================================

            // 获取类名称
            String className = ClassNameReader.getClassName(new ClassReader(b));
            
            // 获取保护域，什么是保护域？
            // 当类装载器将类型装入Java虚拟机时，它们将为每个类型指派一个保护域。
            // 保护域定义了授予一段特定代码的所有权限。（一个保护域对应策略文件中的一个或多个Grant子句。）
            // 装载入Java虚拟机的每一个类型都属于一个且仅属于一个保护域。
            ProtectionDomain protectionDomain = getProtectionDomain();
            
            // 通过 类名称、字节码、保护域 将代理类加载到 JVM 中
            synchronized (classLoader) { // just in case
                if (protectionDomain == null) {
                    gen = ReflectUtils.defineClass(className, b, classLoader);
                } else {
                    gen = ReflectUtils.defineClass(className, b, classLoader, protectionDomain);
                }
            }
            
            // 返回代理类
            return gen;
        } catch (Exception e) {
            //...
        } finally {
            CURRENT.set(save);
        }
    }
}
```

以上代码的核心请参见注释，我们继续看 `strategy.generate(this)` 的源码。

```java
public class DefaultGeneratorStrategy implements GeneratorStrategy {
    public byte[] generate(ClassGenerator cg) throws Exception {
        DebuggingClassWriter cw = getClassVisitor();
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 入口在此 ↓↓↓↓↓↓↓↓↓↓↓↓↓
        transform(cg).generateClass(cw);
        // ===================================
        return transform(cw.toByteArray());
    }
}
```

以上代码的核心请参见注释，我们继续看 `transform(cg).generateClass(cw)` 的源码。

> 前面有说过 `Enhancer.generateClass()` 很有可能就是最终动态生成代理类字节码文件的方法，果然没有猜错。

```java
public class Enhancer extends AbstractClassGenerator {
    // ...
    public void generateClass(ClassVisitor v) throws Exception {
        // 获取父类
        Class sc = (superclass == null) ? Object.class : superclass;
        // 假如父类的修饰符为 final，则抛出异常
        if (TypeUtils.isFinal(sc.getModifiers()))
            throw new IllegalArgumentException("Cannot subclass final class " + sc.getName());
        // 获取父类的构造函数
        List constructors = new ArrayList(Arrays.asList(sc.getDeclaredConstructors()));
        filterConstructors(sc, constructors);

        // 顺序是非常重要的: 必须添加父类, 然后它的父类链, 然后每个接口和父类的接口。
        List actualMethods = new ArrayList();
        List interfaceMethods = new ArrayList();
        final Set forcePublic = new HashSet();
        
        // 过滤父类 sc 的信息，塞到 actualMethods 中
        getMethods(sc, interfaces, actualMethods, interfaceMethods, forcePublic);

        // 将 actualMethods 转换成 MethodInfo 塞到 methods 中
        List methods = CollectionUtils.transform(actualMethods, new Transformer() {
            public Object transform(Object value) {
                Method method = (Method)value;
                int modifiers = Constants.ACC_FINAL
                    | (method.getModifiers()
                       & ~Constants.ACC_ABSTRACT
                       & ~Constants.ACC_NATIVE
                       & ~Constants.ACC_SYNCHRONIZED);
                if (forcePublic.contains(MethodWrapper.create(method))) {
                    modifiers = (modifiers & ~Constants.ACC_PROTECTED) | Constants.ACC_PUBLIC;
                }
                return ReflectUtils.getMethodInfo(method, modifiers);
            }
        });

        // 创建类发射器 e，继承于 org.objectweb.asm.ClassVisitor，用于生成 java字节码
        ClassEmitter e = new ClassEmitter(v);
        
        // ↓↓↓↓↓↓↓↓↓↓↓↓↓ 开始生成字节码 ↓↓↓↓↓↓↓↓↓↓↓↓↓
        
        // 源码对照：public class RealSubject$$EnhancerByCGLIB$$ac3292ae extends RealSubject implements Factory
        if (currentData == null) {
            e.begin_class(Constants.V1_2,
                      Constants.ACC_PUBLIC,
                      getClassName(),
                      Type.getType(sc),
                      (useFactory ?
                       TypeUtils.add(TypeUtils.getTypes(interfaces), FACTORY) :
                       TypeUtils.getTypes(interfaces)),
                      Constants.SOURCE_FILE);
        } else {
            e.begin_class(Constants.V1_2,
                    Constants.ACC_PUBLIC,
                    getClassName(),
                    null,
                    new Type[]{FACTORY},
                    Constants.SOURCE_FILE);
        }
        List constructorInfo = CollectionUtils.transform(constructors, MethodInfoTransformer.getInstance());

        // 源码对照：private boolean CGLIB$BOUND;
        e.declare_field(Constants.ACC_PRIVATE, BOUND_FIELD, Type.BOOLEAN_TYPE, null);
        
        // 源码对照：public static Object CGLIB$FACTORY_DATA;
        e.declare_field(Constants.ACC_PUBLIC | Constants.ACC_STATIC, FACTORY_DATA_FIELD, OBJECT_TYPE, null);
        if (!interceptDuringConstruction) {
            e.declare_field(Constants.ACC_PRIVATE, CONSTRUCTED_FIELD, Type.BOOLEAN_TYPE, null);
        }
        
        // 源码对照：private static final ThreadLocal CGLIB$THREAD_CALLBACKS;
        e.declare_field(Constants.PRIVATE_FINAL_STATIC, THREAD_CALLBACKS_FIELD, THREAD_LOCAL, null);
        
        // 源码对照：private static final Callback[] CGLIB$STATIC_CALLBACKS;
        e.declare_field(Constants.PRIVATE_FINAL_STATIC, STATIC_CALLBACKS_FIELD, CALLBACK_ARRAY, null);
        if (serialVersionUID != null) {
            e.declare_field(Constants.PRIVATE_FINAL_STATIC, Constants.SUID_FIELD_NAME, Type.LONG_TYPE, serialVersionUID);
        }
        
        // 源码对照：private MethodInterceptor CGLIB$CALLBACK_0;
        for (int i = 0; i < callbackTypes.length; i++) {
            e.declare_field(Constants.ACC_PRIVATE, getCallbackField(i), callbackTypes[i], null);
        }
        
        // 源码对照：private static Object CGLIB$CALLBACK_FILTER;
        e.declare_field(Constants.ACC_PRIVATE | Constants.ACC_STATIC, CALLBACK_FILTER_FIELD, OBJECT_TYPE, null);

        if (currentData == null) {
            // 源码对照：final void CGLIB$hello$0(String var1) ...
            emitMethods(e, methods, actualMethods);
            // 源码对照：public RealSubject$$EnhancerByCGLIB$$ac3292ae() ...
            emitConstructors(e, constructorInfo);
        } else {
            emitDefaultConstructor(e);
        }
        
        // 源码对照：public static void CGLIB$SET_THREAD_CALLBACKS(Callback[] var0) ...
        emitSetThreadCallbacks(e);
        // 源码对照：public static void CGLIB$SET_STATIC_CALLBACKS(Callback[] var0) ...
        emitSetStaticCallbacks(e);
        // 源码对照：private static final void CGLIB$BIND_CALLBACKS(Object var0) ...
        emitBindCallbacks(e);

        // 源码对照：
        if (useFactory || currentData != null) {
            int[] keys = getCallbackKeys();
            // 源码对照：public Object newInstance(Callback[] var1) ...
            emitNewInstanceCallbacks(e);
            // 源码对照：public Object newInstance(Callback var1) ...
            emitNewInstanceCallback(e);
            // 源码对照：public Object newInstance(Class[] var1, Object[] var2, Callback[] var3) ...
            emitNewInstanceMultiarg(e, constructorInfo);
            // 源码对照：public Callback getCallback(int var1) ...
            emitGetCallback(e, keys);
            // 源码对照：public void setCallback(int var1, Callback var2) ...
            emitSetCallback(e, keys);
            // 源码对照：public Callback[] getCallbacks() ...
            emitGetCallbacks(e);
            // 源码对照：public void setCallbacks(Callback[] var1) ...
            emitSetCallbacks(e);
        }

        e.end_class();
        
        // ===================================
    }
}
```

以上代码的核心请参见注释，我们对照自动生成代理类 `RealSubject$$EnhancerByCGLIB$$ac3292ae` 的源码，确实能完全匹配函数 `Enhancer.generateClass()` 每个步骤。

```java
package net.sf.cglib.samples.proxypattern;

import java.lang.reflect.Method;
import net.sf.cglib.core.ReflectUtils;
import net.sf.cglib.core.Signature;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class RealSubject$$EnhancerByCGLIB$$ac3292ae extends RealSubject implements Factory {
    private boolean CGLIB$BOUND;
    public static Object CGLIB$FACTORY_DATA;
    private static final ThreadLocal CGLIB$THREAD_CALLBACKS;
    private static final Callback[] CGLIB$STATIC_CALLBACKS;
    private MethodInterceptor CGLIB$CALLBACK_0;
    private static Object CGLIB$CALLBACK_FILTER;
    private static final Method CGLIB$hello$0$Method;
    private static final MethodProxy CGLIB$hello$0$Proxy;
    private static final Object[] CGLIB$emptyArgs;
    private static final Method CGLIB$doSomething$1$Method;
    private static final MethodProxy CGLIB$doSomething$1$Proxy;
    private static final Method CGLIB$equals$2$Method;
    private static final MethodProxy CGLIB$equals$2$Proxy;
    private static final Method CGLIB$toString$3$Method;
    private static final MethodProxy CGLIB$toString$3$Proxy;
    private static final Method CGLIB$hashCode$4$Method;
    private static final MethodProxy CGLIB$hashCode$4$Proxy;
    private static final Method CGLIB$clone$5$Method;
    private static final MethodProxy CGLIB$clone$5$Proxy;

    static void CGLIB$STATICHOOK1() {
        CGLIB$THREAD_CALLBACKS = new ThreadLocal();
        CGLIB$emptyArgs = new Object[0];
        Class var0 = Class.forName("net.sf.cglib.samples.proxypattern.RealSubject$$EnhancerByCGLIB$$ac3292ae");
        Class var1;
        Method[] var10000 = ReflectUtils.findMethods(new String[]{"hello", "(Ljava/lang/String;)V", "doSomething", "()V"}, (var1 = Class.forName("net.sf.cglib.samples.proxypattern.RealSubject")).getDeclaredMethods());
        CGLIB$hello$0$Method = var10000[0];
        CGLIB$hello$0$Proxy = MethodProxy.create(var1, var0, "(Ljava/lang/String;)V", "hello", "CGLIB$hello$0");
        CGLIB$doSomething$1$Method = var10000[1];
        CGLIB$doSomething$1$Proxy = MethodProxy.create(var1, var0, "()V", "doSomething", "CGLIB$doSomething$1");
        var10000 = ReflectUtils.findMethods(new String[]{"equals", "(Ljava/lang/Object;)Z", "toString", "()Ljava/lang/String;", "hashCode", "()I", "clone", "()Ljava/lang/Object;"}, (var1 = Class.forName("java.lang.Object")).getDeclaredMethods());
        CGLIB$equals$2$Method = var10000[0];
        CGLIB$equals$2$Proxy = MethodProxy.create(var1, var0, "(Ljava/lang/Object;)Z", "equals", "CGLIB$equals$2");
        CGLIB$toString$3$Method = var10000[1];
        CGLIB$toString$3$Proxy = MethodProxy.create(var1, var0, "()Ljava/lang/String;", "toString", "CGLIB$toString$3");
        CGLIB$hashCode$4$Method = var10000[2];
        CGLIB$hashCode$4$Proxy = MethodProxy.create(var1, var0, "()I", "hashCode", "CGLIB$hashCode$4");
        CGLIB$clone$5$Method = var10000[3];
        CGLIB$clone$5$Proxy = MethodProxy.create(var1, var0, "()Ljava/lang/Object;", "clone", "CGLIB$clone$5");
    }

    final void CGLIB$hello$0(String var1) {
        super.hello(var1);
    }

    public final void hello(String var1) {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            var10000.intercept(this, CGLIB$hello$0$Method, new Object[]{var1}, CGLIB$hello$0$Proxy);
        } else {
            super.hello(var1);
        }
    }

    final void CGLIB$doSomething$1() {
        super.doSomething();
    }

    public final void doSomething() {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            var10000.intercept(this, CGLIB$doSomething$1$Method, CGLIB$emptyArgs, CGLIB$doSomething$1$Proxy);
        } else {
            super.doSomething();
        }
    }

    final boolean CGLIB$equals$2(Object var1) {
        return super.equals(var1);
    }

    public final boolean equals(Object var1) {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            Object var2 = var10000.intercept(this, CGLIB$equals$2$Method, new Object[]{var1}, CGLIB$equals$2$Proxy);
            return var2 == null ? false : ((Boolean)var2).booleanValue();
        } else {
            return super.equals(var1);
        }
    }

    final String CGLIB$toString$3() {
        return super.toString();
    }

    public final String toString() {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        return var10000 != null ? (String)var10000.intercept(this, CGLIB$toString$3$Method, CGLIB$emptyArgs, CGLIB$toString$3$Proxy) : super.toString();
    }

    final int CGLIB$hashCode$4() {
        return super.hashCode();
    }

    public final int hashCode() {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            Object var1 = var10000.intercept(this, CGLIB$hashCode$4$Method, CGLIB$emptyArgs, CGLIB$hashCode$4$Proxy);
            return var1 == null ? 0 : ((Number)var1).intValue();
        } else {
            return super.hashCode();
        }
    }

    final Object CGLIB$clone$5() throws CloneNotSupportedException {
        return super.clone();
    }

    protected final Object clone() throws CloneNotSupportedException {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        return var10000 != null ? var10000.intercept(this, CGLIB$clone$5$Method, CGLIB$emptyArgs, CGLIB$clone$5$Proxy) : super.clone();
    }

    public static MethodProxy CGLIB$findMethodProxy(Signature var0) {
        String var10000 = var0.toString();
        switch(var10000.hashCode()) {
        case -2084786067:
            if (var10000.equals("hello(Ljava/lang/String;)V")) {
                return CGLIB$hello$0$Proxy;
            }
            break;
        case -508378822:
            if (var10000.equals("clone()Ljava/lang/Object;")) {
                return CGLIB$clone$5$Proxy;
            }
            break;
        case 1826985398:
            if (var10000.equals("equals(Ljava/lang/Object;)Z")) {
                return CGLIB$equals$2$Proxy;
            }
            break;
        case 1913648695:
            if (var10000.equals("toString()Ljava/lang/String;")) {
                return CGLIB$toString$3$Proxy;
            }
            break;
        case 1984935277:
            if (var10000.equals("hashCode()I")) {
                return CGLIB$hashCode$4$Proxy;
            }
            break;
        case 2121560294:
            if (var10000.equals("doSomething()V")) {
                return CGLIB$doSomething$1$Proxy;
            }
        }

        return null;
    }

    public RealSubject$$EnhancerByCGLIB$$ac3292ae() {
        CGLIB$BIND_CALLBACKS(this);
    }

    public static void CGLIB$SET_THREAD_CALLBACKS(Callback[] var0) {
        CGLIB$THREAD_CALLBACKS.set(var0);
    }

    public static void CGLIB$SET_STATIC_CALLBACKS(Callback[] var0) {
        CGLIB$STATIC_CALLBACKS = var0;
    }

    private static final void CGLIB$BIND_CALLBACKS(Object var0) {
        RealSubject$$EnhancerByCGLIB$$ac3292ae var1 = (RealSubject$$EnhancerByCGLIB$$ac3292ae)var0;
        if (!var1.CGLIB$BOUND) {
            var1.CGLIB$BOUND = true;
            Object var10000 = CGLIB$THREAD_CALLBACKS.get();
            if (var10000 == null) {
                var10000 = CGLIB$STATIC_CALLBACKS;
                if (CGLIB$STATIC_CALLBACKS == null) {
                    return;
                }
            }

            var1.CGLIB$CALLBACK_0 = (MethodInterceptor)((Callback[])var10000)[0];
        }

    }

    public Object newInstance(Callback[] var1) {
        CGLIB$SET_THREAD_CALLBACKS(var1);
        RealSubject$$EnhancerByCGLIB$$ac3292ae var10000 = new RealSubject$$EnhancerByCGLIB$$ac3292ae();
        CGLIB$SET_THREAD_CALLBACKS((Callback[])null);
        return var10000;
    }

    public Object newInstance(Callback var1) {
        CGLIB$SET_THREAD_CALLBACKS(new Callback[]{var1});
        RealSubject$$EnhancerByCGLIB$$ac3292ae var10000 = new RealSubject$$EnhancerByCGLIB$$ac3292ae();
        CGLIB$SET_THREAD_CALLBACKS((Callback[])null);
        return var10000;
    }

    public Object newInstance(Class[] var1, Object[] var2, Callback[] var3) {
        CGLIB$SET_THREAD_CALLBACKS(var3);
        RealSubject$$EnhancerByCGLIB$$ac3292ae var10000 = new RealSubject$$EnhancerByCGLIB$$ac3292ae;
        switch(var1.length) {
        case 0:
            var10000.<init>();
            CGLIB$SET_THREAD_CALLBACKS((Callback[])null);
            return var10000;
        default:
            throw new IllegalArgumentException("Constructor not found");
        }
    }

    public Callback getCallback(int var1) {
        CGLIB$BIND_CALLBACKS(this);
        MethodInterceptor var10000;
        switch(var1) {
        case 0:
            var10000 = this.CGLIB$CALLBACK_0;
            break;
        default:
            var10000 = null;
        }

        return var10000;
    }

    public void setCallback(int var1, Callback var2) {
        switch(var1) {
        case 0:
            this.CGLIB$CALLBACK_0 = (MethodInterceptor)var2;
        default:
        }
    }

    public Callback[] getCallbacks() {
        CGLIB$BIND_CALLBACKS(this);
        return new Callback[]{this.CGLIB$CALLBACK_0};
    }

    public void setCallbacks(Callback[] var1) {
        this.CGLIB$CALLBACK_0 = (MethodInterceptor)var1[0];
    }

    static {
        CGLIB$STATICHOOK1();
    }
}
```

经过一番源码探索，我们终于明白了 *cglib* 是如何自动生成代理类的 .class 文件。相比 *jdk代理* 而言，`cglib` 动态代理的整个过程更加复杂，但功能也更加强大。下回我们来对比 *jdk代理* 和 *cglib* 两者的性能差异，深挖两种动态代理的适用场景。
