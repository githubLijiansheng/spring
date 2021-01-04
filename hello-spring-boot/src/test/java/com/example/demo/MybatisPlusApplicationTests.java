package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.test.User;
import com.example.demo.test.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author jiansheng.li
 * @date 2020/12/30 14:41
 */
@SpringBootTest class MybatisPlusApplicationTests {
    // 继承了BaseMapper，所有的方法都来自己父类
    // 我们也可以编写自己的扩展方法！
    @Autowired private UserMapper userMapper;

    @Test void contextLoads() {
        // 参数是一个 Wrapper ，条件构造器，这里我们先设置条件为空，查询所有。
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test public void testInsert() {
        User user = new User();
        user.setName("kwhua_mybatis-plus_insertTest");
        user.setAge(15);
        user.setEmail("310697723@qq.com");

        int result = userMapper.insert(user); // 帮我们自动生成id
        System.out.println(result); // 受影响的行数
        System.out.println(user); // 看到id会自动填充。
    }

    @Test public void testUpdate() {
        User user = new User();
        // 通过条件自动拼接动态sql
        user.setId(1344173111619198979L);
        user.setName("kwhua_mybatis-plus_updateTest");
        user.setAge(20);
        // 注意：updateById 但是参数是一个对象！
        int i = userMapper.updateById(user);
        System.out.println(i);
    }

    @Test public void testOptimisticLocker2() {

        // 线程 1
        User user = userMapper.selectById(1L);
        user.setName("kwhua111");
        user.setEmail("123456@qq.com");

        // 模拟另外一个线程执行了插队操作
        User user2 = userMapper.selectById(1L);
        user2.setName("kwhua222");
        user2.setEmail("123456@qq.com");
        userMapper.updateById(user2);

        // 自旋锁来多次尝试提交！
        int i = userMapper.updateById(user);// 如果没有乐观锁就会覆盖插队线程的值！
        if (i == 0) {
            userMapper.updateById(user.setVersion(user.getVersion() + 1));
        }
    }
    // 测试分页查询
    @Test
    public void testPage(){
        // 参数一：当前页
        // 参数二：页面大小
        Page<User> page = new Page<>(2,5);
        userMapper.selectPage(page,null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getTotal());
    }
    // 测试删除
    @Test
    public void testDeleteById(){
        userMapper.deleteById(1L);
    }

    // 通过id批量删除
    @Test
    public void testDeleteBatchId(){
        userMapper.deleteBatchIds(Arrays.asList(2L,3L));
    }

    // 通过map删除
    @Test
    public void testDeleteMap(){
        HashMap<String, Object> map = new HashMap<>();
        map.put("name","kwhua");
        userMapper.deleteByMap(map);
    }
    @Test
    void contextLoads1() {
        // 查询name不为空的用户，并且邮箱不为空的用户，年龄大于等于12
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .isNotNull("name") //不为空
                .isNotNull("email")
                .ge("age",18);
        userMapper.selectList(wrapper).forEach(System.out::println); // 和我们刚才学习的map对比一下
    }


}