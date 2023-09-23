package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.pojo.EbookSnapshot;
import com.gec._03_wiki.pojo.StatisticResp;
import com.gec._03_wiki.service.EbookSnapshotService;
import com.gec._03_wiki.mapper.EbookSnapshotMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 751200
* @description 针对表【ebook_snapshot】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class EbookSnapshotServiceImpl extends ServiceImpl<EbookSnapshotMapper, EbookSnapshot>
    implements EbookSnapshotService{
    @Override
    public void genSnapshot() {
        this.baseMapper.genSnapshot();
    }

    @Override
    public List<StatisticResp> getStatistic() {
        return this.baseMapper.getStatistic();
    }

    @Override
    public List<StatisticResp> get30Statistic() {
        return this.baseMapper.get30Statistic();
    }
}




