package com.spring.share.aop.message.service.impl;

import com.spring.share.aop.message.bean.Message;
import com.spring.share.aop.message.service.MessageService;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 15:00
 **/
public class MessageServiceImpl implements MessageService {

    @Override
    public void sendMessage(Message message) {
        System.out.println(message);
    }

    @Override
    public String sayHi(String name) {
        System.out.println("接收到参数：" + name);
        return "Hello, " + name;
    }
}
