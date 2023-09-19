package com.gec._03_wiki.service;

import com.gec._03_wiki.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gec._03_wiki.pojo.req.UserLoginReq;
import com.gec._03_wiki.pojo.req.UserQueryReq;
import com.gec._03_wiki.pojo.req.UserResetPasswordReq;
import com.gec._03_wiki.pojo.req.UserSaveReq;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.pojo.resp.UserLoginResp;
import com.gec._03_wiki.pojo.resp.UserQueryResp;

/**
* @author 751200
* @description 针对表【user】的数据库操作Service
* @createDate 2023-09-08 09:50:00
*/
public interface UserService extends IService<User> {

    PageResp<UserQueryResp> getUserListByPage(UserQueryReq req);

    void saveUser(UserSaveReq req);

    void resetPassword(UserResetPasswordReq req);

    UserLoginResp userLogin(UserLoginReq req);

    void logout(String token);
}
