package com.jiegeshe.proxy.pattern.sample.staticproxy;

import com.jiegeshe.proxy.pattern.sample.RealSubject;
import com.jiegeshe.proxy.pattern.sample.Subject;

/**
 * @author stone
 * @version 1.0.0
 * @since 1.0.0 (2018-09-17)
 */
public class InterfaceClient {

    public static void main(String[] args) {
        Subject proxySubject = new InterfaceStaticProxy(new RealSubject());
        proxySubject.doSomething();
        System.out.println("----------------");
        proxySubject.hello("stone");
    }
}
