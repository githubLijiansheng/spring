package com.spring.share.aop.advice;

import org.springframework.aop.AfterReturningAdvice;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 14:36
 **/
public class AfterAdvice implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("方法:" + method.getName() + "被执行，参数: " + args[0] + ", 目标对象: " + target.getClass() + ", 返回结果: " + returnValue);
    }
}
