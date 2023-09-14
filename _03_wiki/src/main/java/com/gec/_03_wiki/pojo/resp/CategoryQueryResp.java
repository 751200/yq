package com.gec._03_wiki.pojo.resp;

import lombok.Data;

@Data
public class CategoryQueryResp {
    private Long id;
    private Long parent;
    private String name;
    private Integer sort;
}
