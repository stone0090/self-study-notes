package com.jiegeshe.proxy.pattern.sample.dynamicproxy;

import java.lang.reflect.Method;

import com.jiegeshe.proxy.pattern.sample.RealSubject;
import com.jiegeshe.proxy.pattern.sample.Subject;
import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-18)
 */
public class CglibProxyClient {

    public static void main(String[] args) throws Exception {

        // 输出 cglib代理 动态生成的 .class字节码 到 com.sun.proxy 路径下
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY,
            System.getProperty("user.dir") + "/proxy-pattern-sample/src/main/java");

        Subject subject = new RealSubject();

        // 创建cglib「基于继承」动态代理类的实例
        Subject superClassProxySubject = createCglibSuperClassDynamicProxy(subject);
        superClassProxySubject.doSomething();
        System.out.println("----------------");
        superClassProxySubject.hello("stone");
        System.out.println("");

        // 创建cglib「基于接口」动态代理类的实例
        Subject interfaceDynamicProxySubject = createCglibInterfaceDynamicProxy(subject);
        interfaceDynamicProxySubject.doSomething();
        System.out.println("----------------");
        interfaceDynamicProxySubject.hello("stone");
    }

    private static Subject createCglibSuperClassDynamicProxy(final Subject delegate) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibSuperClassInterceptor(delegate));
        enhancer.setSuperclass(RealSubject.class);
        return (Subject)enhancer.create();
    }

    private static class CglibSuperClassInterceptor implements MethodInterceptor {

        final Object delegate;

        CglibSuperClassInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object intercept(Object object, Method method, Object[] objects,
                                MethodProxy methodProxy) throws Throwable {
            System.out.println("before cglib interface proxy");
            Object result;
            try {
                result = methodProxy.invokeSuper(object, objects);
            } catch (Exception ex) {
                System.out.println("ex: " + ex.getMessage());
                throw ex;
            } finally {
                System.out.println("after cglib interface proxy");
            }
            return result;
        }
    }

    private static Subject createCglibInterfaceDynamicProxy(final Subject delegate) throws Exception {
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new CglibInterfaceInterceptor(delegate));
        enhancer.setInterfaces(new Class[] {Subject.class});
        return (Subject)enhancer.create();
    }

    private static class CglibInterfaceInterceptor implements MethodInterceptor {

        final Object delegate;

        CglibInterfaceInterceptor(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object intercept(Object object, Method method, Object[] objects,
                                MethodProxy methodProxy) throws Throwable {
            System.out.println("before cglib super class proxy");
            Object result;
            try {
                result = methodProxy.invoke(this.delegate, objects);
            } catch (Exception ex) {
                System.out.println("ex: " + ex.getMessage());
                throw ex;
            } finally {
                System.out.println("after cglib super class proxy");
            }
            return result;
        }
    }

}
