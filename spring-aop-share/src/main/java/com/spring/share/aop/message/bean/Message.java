package com.spring.share.aop.message.bean;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/16 20:01
 **/
public class Message {

    private Integer id;
    private String content;

    public Message(Integer id, String content) {
        this.id = id;
        this.content = content;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
