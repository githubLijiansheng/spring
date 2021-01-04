package com.spring.share.aop.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 15:25
 **/
public class LoggerInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("Before: invocation=[" + invocation.getMethod() + ", " + invocation.getThis() + "," + invocation.getArguments());
        // 执行 真实实现类 的方法
        Object ret = invocation.proceed();
        System.out.println("Invocation returned");
        return ret;
    }
}
