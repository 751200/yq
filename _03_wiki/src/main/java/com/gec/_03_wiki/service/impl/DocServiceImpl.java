package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.pojo.Doc;
import com.gec._03_wiki.pojo.req.DocQueryReq;
import com.gec._03_wiki.pojo.resp.DocQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.service.DocService;
import com.gec._03_wiki.mapper.DocMapper;
import com.gec._03_wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 751200
* @description 针对表【doc】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc>
    implements DocService{
        //打印日志
        private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

        @Override
        public PageResp<DocQueryReq> getDocListByPage(DocQueryReq req) {
            Page<Doc> page = new Page<>(req.getPage(), req.getSize());
            page = this.page(page);
            List<Doc> list = page.getRecords();

            LOG.info("总行数：{}",page.getTotal()+"");
            LOG.info("总页数：{}",page.getPages()+"");

            List<DocQueryReq> docResps = CopyUtil.copyList(list, DocQueryReq.class);

            PageResp<DocQueryReq> repsPage = new PageResp<>();
            repsPage.setList(docResps);
            repsPage.setTotal(page.getTotal());
            return repsPage;
        }

        @Override
        public List<DocQueryResp> allList(DocQueryReq req) {
            QueryWrapper<Doc> wrapper = new QueryWrapper<>();
            wrapper.orderByAsc("sort");

            if(req.getEbookId()!=null)
                wrapper.eq("ebook_id",req.getEbookId());
            List<Doc> list = this.list(wrapper);

            List<DocQueryResp> resp = CopyUtil.copyList(list, DocQueryResp.class);
            return resp;
        }

}





