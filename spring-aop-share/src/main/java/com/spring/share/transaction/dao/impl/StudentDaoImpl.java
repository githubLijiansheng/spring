package com.spring.share.transaction.dao.impl;

import com.spring.share.transaction.bean.Student;
import com.spring.share.transaction.dao.StudentDao;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/20 09:53
 **/
public class StudentDaoImpl implements StudentDao {


    private JdbcTemplate jdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveStudent(Student student) {
        String sql = "insert into t_user (name, password) values (?, ?)";
        jdbcTemplate.update(sql, student.getName(), student.getPassword());
    }
}
