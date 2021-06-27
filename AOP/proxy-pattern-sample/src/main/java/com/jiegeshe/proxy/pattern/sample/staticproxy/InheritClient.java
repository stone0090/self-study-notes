package com.jiegeshe.proxy.pattern.sample.staticproxy;

import com.jiegeshe.proxy.pattern.sample.RealSubject;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-17)
 */
public class InheritClient {

    public static void main(String[] args) {
        RealSubject proxySubject = new InheritStaticProxy();
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }
}
