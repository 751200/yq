package com.gec._03_wiki.controller;

import com.gec._03_wiki.pojo.req.UserLoginReq;
import com.gec._03_wiki.pojo.req.UserQueryReq;
import com.gec._03_wiki.pojo.req.UserResetPasswordReq;
import com.gec._03_wiki.pojo.req.UserSaveReq;
import com.gec._03_wiki.pojo.resp.CommonResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.pojo.resp.UserLoginResp;
import com.gec._03_wiki.pojo.resp.UserQueryResp;
import com.gec._03_wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @GetMapping("/getUserListByPage")
    public CommonResp getUserListByPage(@Valid UserQueryReq req){
        PageResp<UserQueryResp> respPage = userService.getUserListByPage(req);
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        resp.setContent(respPage);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req){
        CommonResp resp = new CommonResp<>();
        userService.saveUser(req);
        return resp;
    }

    @GetMapping("/remove")
    public CommonResp remove(int id){
        userService.removeById(id);
        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }

    @PostMapping("/resetPassword")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req){
        userService.resetPassword(req);
        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @PostMapping("/userLogin")
    public CommonResp userLogin(@Valid @RequestBody UserLoginReq req){
        CommonResp resp = new CommonResp<>();
        UserLoginResp userLoginResp = userService.userLogin(req);
        resp.setContent(userLoginResp);
        return resp;
    }

    @GetMapping("/logout/{token}")
    public CommonResp logout(@PathVariable String token){
        userService.logout(token);
        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }
}
