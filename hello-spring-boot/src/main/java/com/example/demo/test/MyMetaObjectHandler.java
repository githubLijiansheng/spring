package com.example.demo.test;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author jiansheng.li
 * @date 2020/12/30 14:56
 */
@Component // 一定不要忘记把处理器加到IOC容器中！
public class MyMetaObjectHandler implements MetaObjectHandler {
    private static final Logger log = LoggerFactory.getLogger(MyMetaObjectHandler.class);

    // 插入时的填充策略
    @Override public void insertFill(MetaObject metaObject) {
        log.info("start insert fill.....");
        // setFieldValByName(String fieldName, Object fieldVal, MetaObject metaObject
        this.setFieldValByName("gmt_create", new Date(), metaObject);
        this.setFieldValByName("gmt_modified", new Date(), metaObject);
    }

    // 更新时的填充策略
    @Override public void updateFill(MetaObject metaObject) {
        log.info("start update fill.....");
        this.setFieldValByName("gmt_modified", new Date(), metaObject);
    }
}
