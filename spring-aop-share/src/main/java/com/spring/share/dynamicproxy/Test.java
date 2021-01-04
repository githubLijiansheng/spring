package com.spring.share.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author liu_sj [liu_sj@suixingpay.com]
 * @version v1.0
 * @date 2020/11/19 11:08
 **/
public class Test {

    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        InvocationHandler invocation = new DynamicProxy(userService);
        ClassLoader classLoader = userService.getClass().getClassLoader();
        Class<?>[] interfaces = userService.getClass().getInterfaces();
        UserService proxy = (UserService) Proxy.newProxyInstance(classLoader, interfaces, invocation);
        System.out.println("代理对象: " + proxy.getClass());
        proxy.findOne("张三");
    }
}
