package com.gec._03_wiki.pojo.req;

import lombok.Data;

@Data
public class PageReq {
    private Integer page;
    private Integer size;
}