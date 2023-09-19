package com.gec._03_wiki.pojo.resp;

import lombok.Data;

@Data
public class UserLoginResp {
    private Long id;
    private String loginName;//登录名称
    private String name;//昵称
    private String token;//令牌
}