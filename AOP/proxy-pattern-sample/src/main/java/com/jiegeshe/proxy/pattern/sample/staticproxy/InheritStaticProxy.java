package com.jiegeshe.proxy.pattern.sample.staticproxy;

import com.jiegeshe.proxy.pattern.sample.RealSubject;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-17)
 */
public class InheritStaticProxy extends RealSubject {

    public InheritStaticProxy() {
    }

    @Override
    public void doSomething() {
        System.out.println("before inherit static proxy");
        try {
            super.doSomething();
        } catch (RuntimeException ex) {
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
        } catch (RuntimeException ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after inherit static proxy");
        }
    }
}
