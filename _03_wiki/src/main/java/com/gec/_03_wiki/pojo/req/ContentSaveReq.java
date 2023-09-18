package com.gec._03_wiki.pojo.req;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContentSaveReq {
    private Integer id;
    @NotNull(message = "【内容】不能为空")
    private String content;//内容
}
