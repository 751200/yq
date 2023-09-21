package com.gec._03_wiki.service;

import com.gec._03_wiki.pojo.Doc;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gec._03_wiki.pojo.req.DocQueryReq;
import com.gec._03_wiki.pojo.resp.DocQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;

import java.util.List;

/**
* @author 751200
* @description 针对表【doc】的数据库操作Service
* @createDate 2023-09-08 09:50:00
*/
public interface DocService extends IService<Doc> {
    PageResp<DocQueryReq> getDocListByPage(DocQueryReq req);

    List<DocQueryResp> allList(DocQueryReq req);

    int increaseViewCount(int id);
    int increaseVoteCount(int id);
    int updateEbookInfo();
}
