package com.gec._03_wiki.pojo.resp;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class EbookQueryResp {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    private String name;         //名称
    private Long  category1Id;  //分类1
    private Long category2Id;  //分类2
    private String  description;//描述
    private String cover ;     //封面
    private Integer docCount;     //文档数
    private Integer viewCount;    //阅读数
    private Integer voteCount;    //点赞数
}
