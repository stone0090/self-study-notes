package com.jiegeshe.proxy.pattern.sample;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-17)
 */
public class Client {

    public static void main(String[] args) {
        Subject subject = new RealSubject();
        subject.doSomething();
        subject.hello("stone");
    }
}
