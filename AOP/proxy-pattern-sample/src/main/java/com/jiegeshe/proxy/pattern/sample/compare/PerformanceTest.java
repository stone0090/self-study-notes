package com.jiegeshe.proxy.pattern.sample.compare;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.TimeUnit;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2019-02-18)
 */

@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class PerformanceTest {

    public static void main(String[] args) throws Exception {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, ".//");
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Options opt = new OptionsBuilder()
            .include(PerformanceTest.class.getSimpleName())
            .forks(1)
            .warmupIterations(3)
            .measurementIterations(5)
            .mode(Mode.AverageTime)
            .build();
        new Runner(opt).run();
    }

    static TestService jdkProxy;

    static TestService cglibInterfaceProxy;

    static TestService cglibSuperClassProxy;

    static {
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void init() throws Exception {

        TestService delegate = new TestServiceImpl();
        System.out.println("");

        long time = System.currentTimeMillis();
        jdkProxy = createJdkDynamicProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create JDK Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        cglibInterfaceProxy = createCglibInterfaceDynamicProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create CGLIB Interface Proxy: " + time + " ms");

        time = System.currentTimeMillis();
        cglibSuperClassProxy = createCglibSuperClassDynamicProxy(delegate);
        time = System.currentTimeMillis() - time;
        System.out.println("Create CGLIB Super Class Proxy: " + time + " ms");

    }

    @Benchmark
    public void testjdkProxyThirtyMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            jdkProxy.a0();
            jdkProxy.a1();
            jdkProxy.a2();
            jdkProxy.a3();
            jdkProxy.a4();
            jdkProxy.a5();
            jdkProxy.a6();
            jdkProxy.a7();
            jdkProxy.a8();
            jdkProxy.a9();
            jdkProxy.b0();
            jdkProxy.b1();
            jdkProxy.b2();
            jdkProxy.b3();
            jdkProxy.b4();
            jdkProxy.b5();
            jdkProxy.b6();
            jdkProxy.b7();
            jdkProxy.b8();
            jdkProxy.b9();
            jdkProxy.c0();
            jdkProxy.c1();
            jdkProxy.c2();
            jdkProxy.c3();
            jdkProxy.c4();
            jdkProxy.c5();
            jdkProxy.c6();
            jdkProxy.c7();
            jdkProxy.c8();
            jdkProxy.c9();
        }
    }

    @Benchmark
    public void testCglibInterfaceProxyThirtyMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            cglibInterfaceProxy.a0();
            cglibInterfaceProxy.a1();
            cglibInterfaceProxy.a2();
            cglibInterfaceProxy.a3();
            cglibInterfaceProxy.a4();
            cglibInterfaceProxy.a5();
            cglibInterfaceProxy.a6();
            cglibInterfaceProxy.a7();
            cglibInterfaceProxy.a8();
            cglibInterfaceProxy.a9();
            cglibInterfaceProxy.b0();
            cglibInterfaceProxy.b1();
            cglibInterfaceProxy.b2();
            cglibInterfaceProxy.b3();
            cglibInterfaceProxy.b4();
            cglibInterfaceProxy.b5();
            cglibInterfaceProxy.b6();
            cglibInterfaceProxy.b7();
            cglibInterfaceProxy.b8();
            cglibInterfaceProxy.b9();
            cglibInterfaceProxy.c0();
            cglibInterfaceProxy.c1();
            cglibInterfaceProxy.c2();
            cglibInterfaceProxy.c3();
            cglibInterfaceProxy.c4();
            cglibInterfaceProxy.c5();
            cglibInterfaceProxy.c6();
            cglibInterfaceProxy.c7();
            cglibInterfaceProxy.c8();
            cglibInterfaceProxy.c9();
        }
    }

    @Benchmark
    public void testCglibSuperClassProxyThirtyMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            cglibSuperClassProxy.a0();
            cglibSuperClassProxy.a1();
            cglibSuperClassProxy.a2();
            cglibSuperClassProxy.a3();
            cglibSuperClassProxy.a4();
            cglibSuperClassProxy.a5();
            cglibSuperClassProxy.a6();
            cglibSuperClassProxy.a7();
            cglibSuperClassProxy.a8();
            cglibSuperClassProxy.a9();
            cglibSuperClassProxy.b0();
            cglibSuperClassProxy.b1();
            cglibSuperClassProxy.b2();
            cglibSuperClassProxy.b3();
            cglibSuperClassProxy.b4();
            cglibSuperClassProxy.b5();
            cglibSuperClassProxy.b6();
            cglibSuperClassProxy.b7();
            cglibSuperClassProxy.b8();
            cglibSuperClassProxy.b9();
            cglibSuperClassProxy.c0();
            cglibSuperClassProxy.c1();
            cglibSuperClassProxy.c2();
            cglibSuperClassProxy.c3();
            cglibSuperClassProxy.c4();
            cglibSuperClassProxy.c5();
            cglibSuperClassProxy.c6();
            cglibSuperClassProxy.c7();
            cglibSuperClassProxy.c8();
            cglibSuperClassProxy.c9();
        }
    }

    @Benchmark
    public void testjdkProxyFirstMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            jdkProxy.a0();
        }
    }

    @Benchmark
    public void testCglibInterfaceProxyFirstMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            cglibInterfaceProxy.a0();
        }
    }

    @Benchmark
    public void testCglibSuperClassProxyFirstMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            cglibSuperClassProxy.a0();
        }
    }

    @Benchmark
    public void testjdkProxyLastMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            jdkProxy.c9();
        }
    }

    @Benchmark
    public void testCglibInterfaceProxyLastMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            cglibInterfaceProxy.c9();
        }
    }

    @Benchmark
    public void testCglibSuperClassProxyLastMethod() {
        int count = 10000000;
        for (int i = 0; i < count; i++) {
            cglibSuperClassProxy.c9();
        }
    }

    private static <T extends TestService> TestService createJdkDynamicProxy(final TestService delegate) {
        TestService jdkProxy = (TestService)Proxy
            .newProxyInstance(ClassLoader.getSystemClassLoader(),
                new Class[] {TestService.class},
                new JdkHandler(delegate));
        return jdkProxy;
    }

    private static class JdkHandler implements InvocationHandler {

        final Object delegate;

        JdkHandler(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(Object object, Method method, Object[] objects)
            throws Throwable {
            return method.invoke(delegate, objects);
        }
    }

    private static TestService createCglibInterfaceDynamicProxy(final TestService delegate) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibInterfaceInterceptor(delegate));
        enhancer.setInterfaces(new Class[] {TestService.class});
        TestService testService = (TestService)enhancer.create();
        return testService;
    }

    private static class CglibInterfaceInterceptor implements MethodInterceptor {

        final Object delegate;

        CglibInterfaceInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object intercept(Object object, Method method, Object[] objects,
                                MethodProxy methodProxy) throws Throwable {
            return methodProxy.invoke(delegate, objects);
        }
    }

    private static TestService createCglibSuperClassDynamicProxy(final TestService delegate) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibSuperClassInterceptor(delegate));
        enhancer.setSuperclass(TestServiceImpl.class);
        TestService testService = (TestService)enhancer.create();
        return testService;
    }

    private static class CglibSuperClassInterceptor implements MethodInterceptor {

        final Object delegate;

        CglibSuperClassInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object intercept(Object object, Method method, Object[] objects,
                                MethodProxy methodProxy) throws Throwable {
            return methodProxy.invokeSuper(object, objects);
        }
    }
}
