package com.spring.share.transaction.service.impl;

import com.spring.share.transaction.bean.Student;
import com.spring.share.transaction.dao.StudentDao;
import com.spring.share.transaction.service.StudentService;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/20 10:02
 **/
public class StudentServiceImpl implements StudentService {

    private StudentDao studentDao;

    public StudentDao getStudentDao() {
        return studentDao;
    }

    public void setStudentDao(StudentDao studentDao) {
        this.studentDao = studentDao;
    }

    @Override
    public void saveStudent(Student student) {
        studentDao.saveStudent(student);
    }
}
