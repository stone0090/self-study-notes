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

前面讲过，会带大家徒手用 Java 实现 AOP 框架。但在此之前，我们必须熟练的掌握 **代理模式**、**jdk代理**、**cglib** 这3件武器，才能顺利完成自己专属的 AOP 框架。

代理模式是常用的 Java 设计模式，它的特征是代理类与委托类有同样的接口，代理类主要负责为委托类预处理消息、过滤消息、把消息转发给委托类，以及事后处理消息等。代理类与委托类之间通常会存在关联关系，一个代理类的对象与一个委托类的对象关联，代理类的对象本身并不真正实现服务，而是通过调用委托类的对象的相关方法，来提供特定的服务。按照代理的创建时期，代理类可以分为两种。

+ 静态代理：由程序员或特定工具自动生成源代码，再对其编译。在程序运行前，代理类的 .class字节码 文件就已经存在。
+ 动态代理：在程序运行时，工具类会动态的生成代理类的 .class字节码 缓存在内存中，再运用反射机制，实例化出代理对象。

我们来看一段代码示例。

- 定义一个接口 `Subject` 包含两个方法 `doSomething()` 和 `hello()`。
```java
public interface Subject {

    void doSomething();

    void hello(String name);
}
```
- 定义一个类 `RealSubject` 实现接口 `Subject` 中的方法  `doSomething()` 和 `hello()`。
```java
public class RealSubject implements Subject {

    @Override
    public void doSomething() {
        System.out.println("real subject execute doSomething.");
    }

    @Override
    public void hello(String name) {
        System.out.println("hello " + name + ".");
    }
}
```
- 定义一个类 `Client` 执行方法 `doSomething()` 和 `hello()`。
```java
public class Client {

    public static void main(String[] args) {
        Subject subject = new RealSubject();
        subject.doSomething();
        subject.hello("stone");
    }
}
```
输出结果如下图：
![](http://qn.shisb.com/blog/aop/2/1pool.jpg)
> 假如我们接到需求，在类 `RealSubject` 中每个方法的 *执行前、执行后、异常时* 输出日志，该怎么做呢？

### 静态代理
这是个典型的 AOP 应用场景，可以使用纯 Java 手写的静态代理实现，有两种常用的实现方式 **基于接口的静态代理** 和 **基于继承的静态代理**。
#### 基于接口的静态代理
+ 定义一个代理类 `InterfaceStaticProxy` 实现接口 `Subject`。
+ 在代理类构造函数的入参中，传入类 `RealSubject` 的实例 `realSubject`。
+ 在代理类的方法 `doSomething()` 和 `hello()` 中，调用真实对象 `realSubject` 的方法 `doSomething()` 和 `hello()`，并在方法的 *执行前、执行后、异常时* 输出日志。
```java
public class InterfaceStaticProxy implements Subject {

    private RealSubject realSubject;

    InterfaceStaticProxy(RealSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public void doSomething() {
        System.out.println("before interface static proxy");
        try {
            this.realSubject.doSomething();
        } catch (Exception ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after interface static proxy");
        }
    }

    @Override
    public void hello(String name) {
        System.out.println("before interface static proxy");
        try {
            this.realSubject.hello(name);
        } catch (Exception ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after interface static proxy");
        }
    }
}
```
- 创建代理类的实例 `proxySubject`，执行方法  `doSomething()` 和 `hello()`。
```java
public class InterfaceClient {

    public static void main(String[] args) {
        Subject proxySubject = new InterfaceStaticProxy(new RealSubject());
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }
}
```
输出结果如下图：
![](http://qn.shisb.com/blog/aop/2/2pool.jpg)
#### 基于继承的静态代理
+ 定义一个子类 `InheritStaticProxy` 继承类 `RealSubject`。
+ 在子类的方法 `doSomething()` 和 `hello()` 中，调用父类 `RealSubject` 的方法 `doSomething()` 和 `hello()`，并在方法的 *执行前、执行后、异常时* 输出日志。
```java
public class InheritStaticProxy extends RealSubject {

    @Override
    public void doSomething() {
        System.out.println("before inherit static proxy");
        try {
            super.doSomething();
        } catch (Exception ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after inherit static proxy");
        }
    }

    @Override
    public void hello(String name) {
        System.out.println("before inherit static proxy");
        try {
            super.hello(name);
        } catch (Exception ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after inherit static proxy");
        }
    }
}
```
- 创建代理类的实例 `proxySubject`，执行方法  `doSomething()` 和 `hello()`。
```java
public class InheritClient {

    public static void main(String[] args) {
        RealSubject proxySubject = new InheritStaticProxy();
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }
}
```
输出结果如下图：
![](http://qn.shisb.com/blog/aop/2/3pool.jpg)
#### 以上两种实现的优缺点对比
- 基于接口的静态代理：只能针对有接口的类进行代理。
- 基于继承的静态代理：无法对 static、final 类或方法进行代理。

#### 静态代理的缺点
静态代理并没有节省工作量，如果有100个方法需要做，还需要再封装100次，还是比较麻烦的。（但如果把这部分工作量，转交给编译器来完成也是可行的，AspectJ 框架就基于编译器来生成代理类代码，后面再具体讲解。)

### 动态代理
为了解决静态代理的缺点，我们引入了动态代理，在程序运行时，工具类会动态的生成代理类的 .class字节码 缓存在内存中，再运用反射机制，实例化出代理对象。有两种常用的实现方式 **jdk代理** 和 **cglib**。

#### jdk代理
+ 定义一个类 `JdkProxy` 实现接口 `InvocationHandler`。
+ 在类 `JdkProxy` 的构造函数的入参中，传入类 `RealSubject` 的实例 `realSubject`。
+ 在类 `JdkProxy` 的方法 `invoke()` 中使用 `method.invoke(this.realSubject, args);` 调用真实对象 `realSubject` 的方法，并在方法的 *执行前、执行后、异常时* 输出日志。
```java
public class JdkProxy implements InvocationHandler {

    private RealSubject realSubject;

    JdkProxy(RealSubject realSubject) {
        this.realSubject = realSubject;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        System.out.println("before jdk dynamic proxy");
        Object result;
        try {
            result = method.invoke(this.realSubject, args);
        } catch (Exception ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after jdk dynamic proxy");
        }
        return result;
    }
}
```
- 创建动态代理类的实例 `proxySubject`，执行方法  `doSomething()` 和 `hello()`。
```java
public class JdkProxyClient {

    public static void main(String[] args) {

        // 输出 jdk代理 动态生成的 .class字节码 到 com.sun.proxy 路径下
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        // 创建动态代理类的实例
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
输出结果如下图：
![](http://qn.shisb.com/blog/aop/2/4pool.jpg)
类 `JdkProxy` 中的方法 `invoke`，是如何被代理类 `proxySubject` 调用的呢？我们一起看看 jdk代理 动态生成的代理类 `$Proxy0` 的源码就能知晓，在代码中加上  `System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");`，就能把动态生成的 .class字节码 输出到 com.sun.proxy 路径下，使用 idea ide 打开 .class 文件会自动反编译为 java 代码，具体如下:
```java
public final class $Proxy0 extends Proxy implements Subject {
    private static Method m1;
    private static Method m3;
    private static Method m2;
    private static Method m4;
    private static Method m0;

    public $Proxy0(InvocationHandler var1) throws  {
        super(var1);
    }
    
    // ...
    
    public final void doSomething() throws  {
        try {
            super.h.invoke(this, m3, (Object[])null);
        } catch (RuntimeException | Error var2) {
            throw var2;
        } catch (Throwable var3) {
            throw new UndeclaredThrowableException(var3);
        }
    }

    public final void hello(String var1) throws  {
        try {
            super.h.invoke(this, m4, new Object[]{var1});
        } catch (RuntimeException | Error var3) {
            throw var3;
        } catch (Throwable var4) {
            throw new UndeclaredThrowableException(var4);
        }
    }
    
    // ...
    
    static {
        try {
            // ...
            m3 = Class.forName("com.jiegeshe.javaframwork.patternproxy.Subject").getMethod("doSomething");
            m4 = Class.forName("com.jiegeshe.javaframwork.patternproxy.Subject").getMethod("hello", Class.forName("java.lang.String"));
        } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
        } catch (ClassNotFoundException var3) {
            throw new NoClassDefFoundError(var3.getMessage());
        }
    }
}
```
- 动态生成的代理类 `$Proxy0` 继承了代理类 `Proxy` 实现了接口 `Subject` 中的方法 `doSomething()` 和 `hello()`。
- 可以看到方法 `doSomething()` 的核心是  `super.h.invoke(this, m3, (Object[])null);`。
```java
public class Proxy implements java.io.Serializable {

    protected InvocationHandler h;

    private Proxy() {
    }

    protected Proxy(InvocationHandler h) {
        Objects.requireNonNull(h);
        this.h = h;
    }
    
    // ...
}
```
- 从父类 `Proxy` 的源码得知，`super.h` 是接口 `InvocationHandler` 的实现，就是类 `JdkProxy`。
- 所以 `super.h.invoke(this, m3, (Object[])null);` 实际是在调用方法 `JdkProxy.invoke()`。
> 总结：jdk代理 动态生成的代码，跟 [基于接口的静态代理]("基于接口的静态代理") 的代码逻辑基本一致。

#### cglib
+ 定义一个类 `CglibProxy` 实现接口 `MethodInterceptor`。
+ 在类 `CglibProxy` 的方法 `intercept()` 中使用 `methodProxy.invokeSuper(o, objects);` 调用父类 `RealSubject` 的方法，并在方法的 *执行前、执行后、异常时* 输出日志。
```java
public class CglibProxy implements MethodInterceptor {

    @Override
    public Object intercept(final Object o, final Method method, final Object[] objects, final MethodProxy methodProxy) throws Throwable {
        System.out.println("before cglib dynamic proxy");
        Object result;
        try {
            result = methodProxy.invokeSuper(o, objects);
        } catch (Exception ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after cglib dynamic proxy");
        }
        return result;
    }
}
```
- 创建一个类生成器 `enhancer`，设置其父类为 `RealSubject.class`，设置其回调函数为 `new CglibProxy()`。
- 创建动态代理类的实例 `proxySubject`，执行方法  `doSomething()` 和 `hello()`。
```java
public class CglibProxyClient {

    public static void main(String[] args) {

        // 创建一个类生成器 ClassGenerator
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(RealSubject.class);
        enhancer.setCallback(new CglibProxy());

        // 创建动态代理类的实例
        RealSubject proxySubject = (RealSubject) enhancer.create();
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }
}
```
输出结果如下图：
![](http://qn.shisb.com/blog/aop/2/5pool.jpg)
类 `CglibProxy` 中的方法 `intercept`，是如何被代理类 `proxySubject` 调用的呢？我们一起看看 jdk代理 动态生成的代理类 `RealSubject$$EnhancerByCGLIB$$9b08a387` 的源码就能知晓。
![](http://qn.shisb.com/blog/aop/2/6pool.jpg)
在抽象类 `AbstractClassGenerator` 的方法 `generate` 中加上 `FileOutputStream`，就能把动态生成的 .class字节码 输出到工程路径下，使用 idea ide 打开 .class 文件会自动反编译为 java 代码，具体如下:
```java
public class RealSubject$$EnhancerByCGLIB$$9b08a387 extends RealSubject implements Factory {
    private static final Method CGLIB$doSomething$0$Method;
    private static final MethodProxy CGLIB$doSomething$0$Proxy;
    private static final Method CGLIB$hello$1$Method;
    private static final MethodProxy CGLIB$hello$1$Proxy;
    // ...

    final void CGLIB$doSomething$0() {
        super.doSomething();
    }

    public final void doSomething() {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }

        if (var10000 != null) {
            var10000.intercept(this, CGLIB$doSomething$0$Method, CGLIB$emptyArgs, CGLIB$doSomething$0$Proxy);
        } else {
            super.doSomething();
        }
    }

    final void CGLIB$hello$1(String var1) {
        super.hello(var1);
    }

    public final void hello(String var1) {
        MethodInterceptor var10000 = this.CGLIB$CALLBACK_0;
        if (this.CGLIB$CALLBACK_0 == null) {
            CGLIB$BIND_CALLBACKS(this);
            var10000 = this.CGLIB$CALLBACK_0;
        }
        
    // ...
}
```
- 动态生成的代理类 `RealSubject$$EnhancerByCGLIB$$9b08a387` 继承了类 `RealSubject` 实现了接口 `Factory`。
- 可以看到方法 `doSomething()` 的核心是  `var10000.intercept(this, CGLIB$doSomething$0$Method, CGLIB$emptyArgs, CGLIB$doSomething$0$Proxy);`。
- 由 `var10000 = this.CGLIB$CALLBACK_0;` 得知，变量 `var10000` 就是类 `CglibProxy` 的实例。
- 所以 `var10000.intercept(...);` 实际是在调用方法 `CglibProxy.intercept()`。
>  总结：cglib 动态生成的代码，跟 [基于继承的静态代理]("基于继承的静态代理")  的代码逻辑基本一致。

#### jdk代理 和 cglib 的优缺点对比
- jdk代理：基于接口实现的代理，只能针对有接口的类进行代理。
- cglib：基于继承实现的代理，无法对 static、final 类或方法进行代理。

### 总结
此次，我们初步认识了 **代理模式**、**jdk代理**、**cglib**，了解了动态代理的实现原理、及其优缺点。后面，我们还会继续深度阅读 **jdk代理**、**cglib** 源码，掌握其精髓，才能顺利完成自己专属的 AOP 框架。
