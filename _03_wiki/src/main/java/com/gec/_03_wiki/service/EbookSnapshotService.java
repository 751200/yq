package com.gec._03_wiki.service;

import com.gec._03_wiki.pojo.EbookSnapshot;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gec._03_wiki.pojo.StatisticResp;

import java.util.List;

/**
* @author 751200
* @description 针对表【ebook_snapshot】的数据库操作Service
* @createDate 2023-09-08 09:50:00
*/
public interface EbookSnapshotService extends IService<EbookSnapshot> {

    void genSnapshot();

    List<StatisticResp> getStatistic();

    List<StatisticResp> get30Statistic();
}
