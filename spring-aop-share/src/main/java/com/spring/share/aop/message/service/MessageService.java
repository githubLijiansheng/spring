package com.spring.share.aop.message.service;

import com.spring.share.aop.message.bean.Message;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 15:00
 **/
public interface MessageService {

    void sendMessage(Message message);

    String sayHi(String name);
}
