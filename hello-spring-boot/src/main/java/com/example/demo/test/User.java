package com.example.demo.test;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

/**
 * @author jiansheng.li
 * @date 2020/12/30 14:39
 */
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
    private String email;
    // 字段添加填充内容
    @TableField(fill = FieldFill.INSERT)
    private Date gmt_create;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date gmt_modified;

    @Version //乐观锁Version注解
    private Integer version;

    @TableLogic //逻辑删除
    private Integer deleted;
    public Long getId() {
        return id;
    }

    public User setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getAge() {
        return age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public User setEmail(String email) {
        this.email = email;
        return this;
    }

    public Date getGmt_create() {
        return gmt_create;
    }

    public User setGmt_create(Date gmt_create) {
        this.gmt_create = gmt_create;
        return this;
    }

    public Date getGmt_modified() {
        return gmt_modified;
    }

    public User setGmt_modified(Date gmt_modified) {
        this.gmt_modified = gmt_modified;
        return this;
    }

    public Integer getVersion() {
        return version;
    }

    public User setVersion(Integer version) {
        this.version = version;
        return this;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public User setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    @Override public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", age=").append(age);
        sb.append(", email='").append(email).append('\'');
        sb.append(", gmt_create=").append(gmt_create);
        sb.append(", gmt_modified=").append(gmt_modified);
        sb.append(", version=").append(version);
        sb.append(", deleted=").append(deleted);
        sb.append('}');
        return sb.toString();
    }
}

