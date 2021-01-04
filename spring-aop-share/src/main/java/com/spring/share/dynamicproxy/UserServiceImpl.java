package com.spring.share.dynamicproxy;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/19 11:04
 **/
public class UserServiceImpl implements UserService{

    @Override
    public String findOne(String name) {
        return "Hello " + name;
    }
}
