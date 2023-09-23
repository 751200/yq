package com.gec._03_wiki.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class StatisticResp {
    @JsonFormat(pattern =  "MM-dd",timezone = "GMT+8")
    private Date date;
    private Integer viewCount;
    private Integer voteCount;
    private Integer viewIncrease;
    private Integer voteIncrease;
}
