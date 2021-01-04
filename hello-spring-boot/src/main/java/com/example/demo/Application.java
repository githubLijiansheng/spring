package com.example.demo;

import com.example.demo.service.RetryService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

/**
 * @author jiansheng.li
 * @date 2020/12/29 14:07
 */
@Configuration @EnableRetry public class Application {
    @Bean public RetryService retryService() {
        return new RetryService();
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext("com.example.demo");
        RetryService service1 = applicationContext.getBean("service", RetryService.class);
        service1.service();
    }
}

