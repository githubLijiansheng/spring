package com.spring.share.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 11:07
 **/
public class DynamicProxy implements InvocationHandler {

    private Object sub;

    public DynamicProxy(Object sub) {
        this.sub = sub;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("proxy :" + proxy.getClass());
        System.out.println("方法执行前，执行的动作");
        Object ret = method.invoke(sub, args);
        System.out.println("方法返回结果:" + ret);
        System.out.println("方法执行后，执行的动作");
        return ret;
    }
}
