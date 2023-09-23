package com.gec._03_wiki.controller;

import com.gec._03_wiki.pojo.StatisticResp;
import com.gec._03_wiki.pojo.resp.CommonResp;
import com.gec._03_wiki.service.EbookSnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Autowired
    private EbookSnapshotService ebookSnapshotService;

    @GetMapping("/getStatistic")
    public CommonResp getStatistic(){
        CommonResp<List<StatisticResp>> resp = new CommonResp<>();
        List<StatisticResp> list = ebookSnapshotService.getStatistic();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/get30Statistic")
    public CommonResp get30Statistic(){
        CommonResp<List<StatisticResp>> resp  = new CommonResp<>();
        List<StatisticResp> list = ebookSnapshotService.get30Statistic();
        resp.setContent(list);
        return resp;
    }
}
