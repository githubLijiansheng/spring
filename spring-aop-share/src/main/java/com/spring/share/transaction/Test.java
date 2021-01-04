package com.spring.share.transaction;

import com.spring.share.transaction.bean.Student;
import com.spring.share.transaction.service.StudentService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * TODO
 *
 * @author liu_sj
 * @version v1.0
 * @date 2020/11/20 09:47
 **/
public class Test {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-tx.xml");
        // 获取 Spring AOP 生成的代理类
        StudentService studentService = (StudentService) context.getBean("studentProxy");
        // StudentService studentService = (StudentService) context.getBean("studentService");

        studentService.saveStudent(new Student("张三", "12345226"));
    }
}
