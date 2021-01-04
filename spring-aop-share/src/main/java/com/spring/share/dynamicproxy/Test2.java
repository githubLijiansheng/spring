package com.spring.share.dynamicproxy;

import java.lang.reflect.Proxy;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 11:37
 **/
public class Test2 {

    public static void main(String[] args) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        UserService proxy = (UserService) Proxy.newProxyInstance(classLoader, new Class[]{UserService.class},
                (pro, method, param) -> "abc");
        System.out.println("代理对象: " + proxy.getClass());
        String str = proxy.findOne("张三");
        System.out.println("方法返回结果: " + str);
    }
}
