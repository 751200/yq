package com.gec._03_wiki.service;

import com.gec._03_wiki.pojo.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gec._03_wiki.pojo.req.CategoryQueryReq;
import com.gec._03_wiki.pojo.resp.CategoryQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;

import java.util.List;

/**
* @author 751200
* @description 针对表【category】的数据库操作Service
* @createDate 2023-09-08 09:50:00
*/
public interface CategoryService extends IService<Category> {
    PageResp<CategoryQueryReq> getCategoryListByPage(CategoryQueryReq req);

    List<CategoryQueryResp> allList(CategoryQueryReq req);
}
