package com.gec._03_wiki.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gec._03_wiki.pojo.req.EbookQueryReq;
import com.gec._03_wiki.pojo.resp.CommonResp;
import com.gec._03_wiki.pojo.Ebook;
import com.gec._03_wiki.pojo.resp.EbookQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.service.EbookService;
import com.gec._03_wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EbookController {

    @Autowired
    private EbookService ebookService;

//    @GetMapping("/ebookAll")
//    public CommonResp getAll(){
//        List<Ebook> list = ebookService.list();
//        CommonResp<List<Ebook>> resp = new CommonResp<>();
//        resp.setContent(list);
//        return resp;
//    }

    @GetMapping("/getEbookByName")
    public CommonResp getEbookByName(EbookQueryReq req){
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like("name",req.getName());
        List<Ebook> list = ebookService.list(wrapper);

//        ArrayList<EbookResp> ebookResps = new ArrayList<>();
//        for (Ebook ebook : list) {
//            EbookResp copy = CopyUtil.copy(ebook, EbookResp.class);
//            ebookResps.add(copy);
//        }
        List<EbookQueryResp> ebookQueryResps = CopyUtil.copyList(list, EbookQueryResp.class);
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        resp.setContent(ebookQueryResps);
        return resp;
    }

    @GetMapping("/getEbookByEbookQueryReq")
    public CommonResp getEbookByEbookQueryReq(EbookQueryReq req){
        List<Ebook> list;
        System.out.println("req = " + req);
        if (!ObjectUtils.isEmpty(req.getName())){
            QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
            wrapper.like("name",req.getName());
            list = ebookService.list(wrapper);
        } else {
            list = ebookService.list();
        }
        List<EbookQueryResp> ebookQueryResps = CopyUtil.copyList(list, EbookQueryResp.class);
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        resp.setContent(ebookQueryResps);
        return resp;
    }

    //打印日志
    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @GetMapping("/getebookListByPage")
    public CommonResp getebookListByPage(EbookQueryReq EbookQueryReq){
        Page<Ebook> page = new Page<>(EbookQueryReq.getPage(),EbookQueryReq.getSize());
        page = ebookService.page(page);
        List<Ebook> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<EbookQueryResp> ebookQueryResps = CopyUtil.copyList(list, EbookQueryResp.class);

        PageResp<EbookQueryResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(ebookQueryResps);

        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        resp.setContent(respPage);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody Ebook req){
//        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        //会根据是否有id判断   修改（有id）  新增（无id）
        ebookService.saveOrUpdate(req);

        CommonResp resp = new CommonResp<>();
        return resp;
    }
}
