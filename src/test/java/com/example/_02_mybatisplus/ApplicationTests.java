package com.example._02_mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example._02_mybatisplus.entity.User;
import com.example._02_mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class ApplicationTests {
    @Autowired
     UserMapper userMapper;
    @Test
    void contexxtLoads(){


        List list = userMapper.selectList(null);

        System.out.println(list.toString());

    }

    @Test
    void contextLoads() {

        User user = new User();
        user.setAge(11);
        user.setName("alex");
        user.setEmail("12345678910@qq.com");

        int result = userMapper.insert(user);
        System.out.println(result);
        System.out.println(user);
    }
// sadasd_sdad ==> sadasdSdad
    @Test
    void upgradeTest(){
        User user = new User();
        user.setId(7L);
        user.setName("v1");

        userMapper.updateById(user);
    }

    @Test
    void optismTest(){
        User user = userMapper.selectById(1L);

        user.setName("v3");
        user.setVersion(user.getVersion());
        userMapper.updateById(user);
    }

    @Test
    void PageTest(){
        Page<User> page =new Page<>(1,5);
        userMapper.selectPage(page,null);
        page.getRecords().forEach(System.out::println);
        System.out.println(page.getCurrent());
        System.out.println(page.getPages());
        System.out.println(page.getSize());
        System.out.println(page.getTotal());
        System.out.println(page.hasNext());
        System.out.println(page.hasPrevious());
    }

    @Test
    void logicDeletedTest() {
        int result = userMapper.deleteById(3L);
        System.out.println(result);
    }

    @Test
    void logicDeletedSelectTest(){
        User user = new User();
        user.setDeleted(0);//查不到所以也改不了
        userMapper.updateById(user);
        user = userMapper.selectById(3L);
        System.out.println(user);
    }

    @Test
    void wrapperTest(){
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper
                .isNull("name")
                .le("age",11);
        userMapper.selectOne(wrapper);
    }
}