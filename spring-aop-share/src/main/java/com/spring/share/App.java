package com.spring.share;

import com.spring.share.aop.message.service.MessageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world!
 */

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application.xml");
        // 获取 Spring AOP 生成的代理类
        MessageService messageService = (MessageService) context.getBean("proxyFactoryBean");
        // messageService.sendMessage(new Message(1122, "消息内容"));
        System.out.println("---------------");
        messageService.sayHi("张三");
    }
}
