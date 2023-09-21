package com.gec._03_wiki.controller;

import com.gec._03_wiki.pojo.Content;
import com.gec._03_wiki.pojo.Doc;
import com.gec._03_wiki.pojo.req.DocQueryReq;
import com.gec._03_wiki.pojo.req.DocSaveReq;
import com.gec._03_wiki.pojo.resp.CommonResp;
import com.gec._03_wiki.pojo.resp.DocQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.service.ContentService;
import com.gec._03_wiki.service.DocService;
import com.gec._03_wiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;

    @Autowired
    private ContentService contentService;

    @GetMapping("/getDocListByPage")
    public CommonResp getDocListByPage(DocQueryReq req){
        PageResp<DocQueryReq> repsPage = docService.getDocListByPage(req);
        CommonResp<PageResp<DocQueryReq>> resp = new CommonResp<>();
        resp.setContent(repsPage);
        return resp;
    }

    @GetMapping("/allList")
    public CommonResp allList(DocQueryReq req){
        List<DocQueryResp> resp = docService.allList(req);
        CommonResp<List<DocQueryResp>> listCommonResp = new CommonResp<>();
        listCommonResp.setContent(resp);
        return listCommonResp;
    }

    @GetMapping("/findContentById/{id}")
    public CommonResp findContentById(@PathVariable int id){
        Content content = contentService.getById(id);

        docService.increaseViewCount(id);
        CommonResp<String> resp = new CommonResp<>();
        if (content!=null && content.getContent()!=null){
            resp.setContent(content.getContent());
        }
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req){
        Doc doc = CopyUtil.copy(req, Doc.class);
        docService.saveOrUpdate(doc);

        Content content = CopyUtil.copy(req, Content.class);
        contentService.saveOrUpdate(content);

        CommonResp resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/remove")
    public CommonResp remove(Integer [] ids){
        List<Integer> list = Arrays.asList(ids);
        docService.removeByIds(list);

        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/vote/{id}")
    public CommonResp vote(@PathVariable int id){
        docService.increaseVoteCount(id);
        CommonResp<String> resp = new CommonResp<>();
        return resp;
    }


}
