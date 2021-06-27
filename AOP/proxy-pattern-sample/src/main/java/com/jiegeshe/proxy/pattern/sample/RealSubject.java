package com.jiegeshe.proxy.pattern.sample;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-17)
 */
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
