package com.spring.share.aop.advice;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 14:36
 **/
public class BeforeAdvice implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("方法:" + method.getName() + "将要被执行，参数: " + args[0] + ", 目标对象: " + target.getClass());
    }
}
