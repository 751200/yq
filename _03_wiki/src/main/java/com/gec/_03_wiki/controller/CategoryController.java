package com.gec._03_wiki.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gec._03_wiki.pojo.Category;
import com.gec._03_wiki.pojo.Ebook;
import com.gec._03_wiki.pojo.req.CategoryQueryReq;
import com.gec._03_wiki.pojo.req.EbookQueryReq;
import com.gec._03_wiki.pojo.resp.CategoryQueryResp;
import com.gec._03_wiki.pojo.resp.CommonResp;
import com.gec._03_wiki.pojo.resp.EbookQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.service.CategoryService;
import com.gec._03_wiki.utils.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getCategoryListByPage")
    public CommonResp getCategoryListByPage(CategoryQueryReq req){
        PageResp<CategoryQueryReq> respPage = categoryService.getCategoryListByPage(req);
        CommonResp<PageResp<CategoryQueryReq>> resp = new CommonResp<>();
        resp.setContent(respPage);
       return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody CategoryQueryReq req){
        Category category = CopyUtil.copy(req,Category.class);
        categoryService.saveOrUpdate(category);

        CommonResp resp = new CommonResp();
        return resp;
    }

    @GetMapping("/remove")
    public CommonResp remove(int id){
        categoryService.removeById(id);
        CommonResp<Object> resp = new CommonResp<>();
        return resp;
    }

    @GetMapping("/allList")
    public CommonResp allList(CategoryQueryReq req){

        List<CategoryQueryResp> resp = categoryService.allList(req);
        CommonResp<List<CategoryQueryResp>> listCommonResp = new CommonResp<>();
        listCommonResp.setContent(resp);
        return listCommonResp;
    }
}
