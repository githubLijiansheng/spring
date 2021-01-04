package com.spring.share.transaction.bean;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/20 09:48
 **/
public class Student {

    private Integer id;
    private String name;
    private String password;

    public Student(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
