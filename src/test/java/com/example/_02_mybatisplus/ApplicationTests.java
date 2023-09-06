package com.example._02_mybatisplus;

import com.example._02_mybatisplus.entity.User;
import com.example._02_mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        user.setName("v");

        userMapper.updateById(user);
    }

}
