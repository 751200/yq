package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.pojo.Category;
import com.gec._03_wiki.pojo.req.CategoryQueryReq;
import com.gec._03_wiki.pojo.resp.CategoryQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.service.CategoryService;
import com.gec._03_wiki.mapper.CategoryMapper;
import com.gec._03_wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
* @author 751200
* @description 针对表【category】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Override
    public PageResp<CategoryQueryReq> getCategoryListByPage(CategoryQueryReq req) {
        Page<Category> page = new Page<>(req.getPage(),req.getSize());
        page = this.page(page);
        List<Category> list = page.getRecords();

        LOG.info("总行数:{}",page.getTotal()+"");
        LOG.info("总页数:{}",page.getTotal()+"");

        List<CategoryQueryReq> categoryQueryReqs = CopyUtil.copyList(list, CategoryQueryReq.class);

        PageResp<CategoryQueryReq> respPage = new PageResp<>();
        respPage.setList(categoryQueryReqs);
        respPage.setTotal(page.getTotal());
        return respPage;
    }

    @Override
    public List<CategoryQueryResp> allList(CategoryQueryReq req) {


        List<Category> list1=new ArrayList<>();


        if(req.getName()!=null&&req.getName()!=""){//把该结点的父结点及子结点一并返回
                HashSet<Category> set = new HashSet<>();
                QueryWrapper<Category> wrapper =new QueryWrapper<>();
                wrapper.like("name",req.getName());
                list1.addAll(this.list(wrapper));

                for(int i=0;i<list1.size();i++){
//                    wrapper.clear();
                    set.add(this.getById(list1.get(i).getParent()));//获取查找节点的父母
//
//                    wrapper.eq("parent",list1.get(i).getId());
//                    set.addAll(this.list(wrapper));//查找该节点的孩子
                }
                set.remove(null);
                set.addAll(list1);
                list1.clear();list1.addAll(set);set.clear();

        }else {
            list1 = this.list();
        }

        List<CategoryQueryResp> list =CopyUtil.copyList(list1,CategoryQueryResp.class);
        return list;
    }
}




