> 本文约 1500 字，读完本文大概需要 5 分钟。
> 
> - [《AOP 挖掘记》概念介绍及原理初探（一）](https://github.com/stone0090/aop-dig-down/blob/master/README.MD)
> - [《AOP 挖掘记》代理模式、jdk代理、cglib（二）](https://github.com/stone0090/aop-dig-down/blob/master/README-AOP-2.md)
> - [《AOP 挖掘记》jdk代理 - 源码解析（三）](https://github.com/stone0090/aop-dig-down/blob/master/README-AOP-3.md)
> - [《AOP 挖掘记》cglib - 源码解析（四）](https://github.com/stone0090/aop-dig-down/blob/master/README-AOP-4.md)
> - 《AOP 挖掘记》jdk代理、cglib 性能对比（五）【未完成】
> - 《AOP 挖掘记》徒手实现 AOP 框架（六）【未完成】
> - 《AOP 挖掘记》Spring AOP 源码解析（七）【未完成】
> - 《AOP 挖掘记》AspectJ AOP 原理解析（八）【未完成】

### AOP 的基本概念
AOP 是 Aspect-Oriented programming 的缩写，中文翻译为面向切面编程，它和 OOP 一样是一种编程思想。

AOP 把软件系统分为两个部分：`核心关注点` 和 `横切关注点`。业务处理的主要流程是核心关注点，与之关系不大的部分是横切关注点。横切关注点的一个特点是，他们经常发生在核心关注点的多处，而各处都基本相似。比如权限认证、日志、事务处理。AOP 的作用在于分离系统中的各种关注点，将核心关注点和横切关注点分离开来。通俗的讲就是将应用程序中的业务逻辑同对其提供支持的通用服务进行分离，AOP 可以说是 OOP 的补充和完善。
![](http://qn.shisb.com/blog/aop/1/1pool.jpg)

使用 OOP 的好处是可以设计出低耦合的系统，使系统更加灵活、更加易于维护，但同时也增加了代码的重复性。比如，我们要打印类中每个方法的出入参。按面向对象的编程思想，就必须在类的每方法中都加入日志打印功能。如下左图所示：

此时引入 AOP 的编程思想，将日志打印功能（切面），动态的切入到类中每个方法的前后（切点），无需改动原有代码，便可改变其原有的行为。如下右图所示：
![](http://qn.shisb.com/blog/aop/1/2pool.jpg)

除此之外，使用 AOP 的编程思想，还能降低模块的耦合度、增强代码的复用性、使系统更容易扩展。

### AOP 的相关术语
![](http://qn.shisb.com/blog/aop/1/3pool.jpg)

以上 AOP 术语在整个环节中的具体位置，如下图所示：
![](http://qn.shisb.com/blog/aop/1/4pool.jpg)

### AOP 的使用场景
![](http://qn.shisb.com/blog/aop/1/5pool.jpg)

### AOP 的实现原理
在了解了 AOP 的基本特征、相关术语、使用场景之后，对设计模式有一定了解的朋友，一定已经猜到 AOP 实际上是基于 `代理模式` 和 `装饰者模式` 实现的。

代理模式和装饰者模式都是常用的 Java 设计模式，它的特征是代理类与委托类有同样的接口，代理类主要负责为委托类预处理消息、过滤消息、把消息转发给委托类，以及事后处理消息等。代理类与委托类之间通常会存在关联关系，一个代理类的对象与一个委托类的对象关联，代理类的对象本身并不真正实现服务，而是通过调用委托类的对象的相关方法，来提供特定的服务。如以下类图所示：
![](http://qn.shisb.com/blog/aop/1/6pool.jpg)

两种模式最主要的区别是：代理模式中，代理类对被代理的对象有控制权，决定其执行或者不执行。而装饰模式中，装饰类对代理对象没有控制权，只能为其增加一层装饰，以加强被装饰对象的功能。

所以，在使用 AOP 对方法进行权限校验时，会影响真实对象是否执行，可以理解为是通过代理模式实现的；在使用 AOP 对方法进行日志追踪时，不会影响真实对象的执行，可以理解为是通过装饰者模式实现的。由于两种模式区别不大，后面统一按代理模式进行表述。

按照代理的创建时期，代理类可以分为两种。
+ 静态代理：由程序员或特定工具自动生成源代码，再对其编译。在程序运行前，代理类的 .class字节码 文件就已经存在。
+ 动态代理：在程序运行时，工具类会动态的生成代理类的 .class字节码 缓存在内存中，再运用反射机制，实例化出代理对象。

### AOP 的具体实现
上面有讲过，切面织入的方式有3种，分别是：
- 运行时织入（Runtime wearing）：是指采用 jdk代理 或 cglib 工具进行切面的织入。
- 编译期织入（Compile time wearing）：是指在 Java 编译期，采用特殊的编译器，将切面织入到 Java 类中。
- 类加载期织入（Classload time wearing）：是指通过特殊的类加载器，在类字节码加载到 JVM 时织入切面。

我们最常用的 Spring AOP 是采用运行时织入（Runtime wearing），它是基于动态代理的实现的。如果需要代理的对象，实现了某个接口，那么 Spring AOP 会使用 jdk代理 去创建代理对象，而对于没有实现接口的对象，Spring AOP 会使用 cglib 生成一个被代理对象的子类，如下图所示：
![](http://qn.shisb.com/blog/aop/1/7pool.jpg)

Spring AOP 的目的并不是为了提供最完整的 AOP 实现，而是为了要帮助解决企业应用中的常见问题，提供一个 AOP 实现与 Spring IOC 之间的紧密集成。如果你计划在 Spring Beans 之上将横切关注点模块化，那么 Spring AOP 是你的首选。但如果你用的是普通的 Java 对象而不是 Spring beans，又或者是想支持全部的 Pointcut 类型，那么你就需要引入更完备的 AOP 框架 AspectJ 了。

AspectJ 是一个面向切面的框架，它无缝扩展了 Java 语言，它有一个专门的编译器用来生成遵守 Java 字节编码规范的 Class 文件。它采用编译期织入（Compile time wearing）和类加载期织入（Classload time wearing），它是基于静态代理的实现的，是语言级的 AOP 实现，提供了完备的 AOP 支持。编译期织入过程，如下图所示：
![](http://qn.shisb.com/blog/aop/1/8pool.jpg)

### 总结

除了 Spring AOP 和 AspectJ 之外，还有很多开源框架也包含了 AOP 的实现，例如：Hibernate、dynaop等，大家只要明白了其实现原理，就可以举一反三。后续我会带着大家徒手实现简单的 AOP 框架，敬请期待。
