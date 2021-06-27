package com.jiegeshe.proxy.pattern.sample.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.jiegeshe.proxy.pattern.sample.RealSubject;
import com.jiegeshe.proxy.pattern.sample.Subject;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-18)
 */
public class JdkProxyClient {

    public static void main(String[] args) {

        // 输出 jdk代理 动态生成的 .class字节码 到 com.sun.proxy 路径下
        System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");

        Subject subject = new RealSubject();

        // 创建动态代理类的实例
        Subject proxySubject = createJdkDynamicProxy(subject);
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }

    private static Subject createJdkDynamicProxy(final Subject delegate) {
        return (Subject)Proxy.newProxyInstance(
            JdkProxyClient.class.getClassLoader(),
            new Class[] {Subject.class},
            new JdkProxyInvocationHandler(delegate));
    }

    private static class JdkProxyInvocationHandler implements InvocationHandler {

        final Object delegate;

        JdkProxyInvocationHandler(Object delegate) {
            this.delegate = delegate;
        }

        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            System.out.println("before jdk dynamic proxy");
            Object result;
            try {
                result = method.invoke(this.delegate, args);
            } catch (Exception ex) {
                System.out.println("ex: " + ex.getMessage());
                throw ex;
            } finally {
                System.out.println("after jdk dynamic proxy");
            }
            return result;
        }
    }



}
