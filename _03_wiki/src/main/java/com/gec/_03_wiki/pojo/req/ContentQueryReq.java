package com.gec._03_wiki.pojo.req;

import lombok.Data;

@Data
public class ContentQueryReq extends PageReq{
    private Integer id;
    private String content;//内容
}
