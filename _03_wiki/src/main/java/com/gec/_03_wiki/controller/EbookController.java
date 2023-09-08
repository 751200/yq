package com.gec._03_wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gec._03_wiki.pojo.CommonResp;
import com.gec._03_wiki.pojo.Ebook;
import com.gec._03_wiki.pojo.EbookReq;
import com.gec._03_wiki.pojo.EbookResp;
import com.gec._03_wiki.service.EbookService;
import com.gec._03_wiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

    @GetMapping("/ebookAll")
    public CommonResp getAll(){
        List<Ebook> list = ebookService.list();
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/getEbookByName")
    public CommonResp getEbookByName(EbookReq req){
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like("name",req.getName());
        List<Ebook> list = ebookService.list(wrapper);

//        ArrayList<EbookResp> ebookResps = new ArrayList<>();
//        for (Ebook ebook : list) {
//            EbookResp copy = CopyUtil.copy(ebook, EbookResp.class);
//            ebookResps.add(copy);
//        }
        List<EbookResp> ebookResps = CopyUtil.copyList(list, EbookResp.class);
        CommonResp<List<EbookResp>> resp = new CommonResp<>();
        resp.setContent(ebookResps);
        return resp;
    }
}
