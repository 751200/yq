package com.gec._03_wiki.pojo.req;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
public class PageReq {

    @NotNull(message = "页码不能为空")
    private Integer page;

    @NotNull(message = "页数不能为空")
    @Max(value = 1000,message = "每页最多显示1000条")
    private Integer size;
}