package com.gec._03_wiki.pojo.req;

import lombok.Data;

@Data
public class UserQueryReq extends PageReq{
    private String loginName;         //名称
}
