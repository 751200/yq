package com.gec._03_wiki.pojo.req;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class EbookQueryReq extends PageReq{
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;


    private String name;
}