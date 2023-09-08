package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.pojo.User;
import com.gec._03_wiki.service.UserService;
import com.gec._03_wiki.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 751200
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




