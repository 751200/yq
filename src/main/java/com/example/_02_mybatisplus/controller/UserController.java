package com.example._02_mybatisplus.controller;

import com.alibaba.fastjson.JSON;
import com.example._02_mybatisplus.entity.User;
import com.example._02_mybatisplus.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/userList")
    public List<User> getList(){

        List<User> list = userMapper.selectList(null);

//        System.out.println(list.toString());
        return list;
    }

    @PostMapping("/user")
    @ResponseBody()
    public void selec(@RequestBody String id){

        User user = JSON.parseObject(id,User.class);
        System.out.println(id);
        System.out.println(user.toString());
    }
}
