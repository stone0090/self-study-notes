package com.jiegeshe.proxy.pattern.sample.staticproxy;

import com.jiegeshe.proxy.pattern.sample.RealSubject;
import com.jiegeshe.proxy.pattern.sample.Subject;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-17)
 */
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
        } catch (RuntimeException ex) {
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
        } catch (RuntimeException ex) {
            System.out.println("ex: " + ex.getMessage());
            throw ex;
        } finally {
            System.out.println("after interface static proxy");
        }
    }
}
