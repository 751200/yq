package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.exception.BusinessException;
import com.gec._03_wiki.exception.BusinessExceptionCode;
import com.gec._03_wiki.mapper.DocMapper;
import com.gec._03_wiki.pojo.Doc;
import com.gec._03_wiki.pojo.req.DocQueryReq;
import com.gec._03_wiki.pojo.resp.DocQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.service.DocService;
import com.gec._03_wiki.service.WsService;
import com.gec._03_wiki.utils.CopyUtil;
import com.gec._03_wiki.utils.RedisUtil;
import com.gec._03_wiki.utils.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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

        @Autowired
        private RedisUtil redisUtil;

        @Autowired
        private WsService wsService;
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

    @Override
    public int increaseViewCount(int id) {
        return this.baseMapper.increaseViewCount(id);
    }
    public int increaseVoteCount(int id) {
            String ip = RequestContext.getRemoteAddr();

            //推送消息
            Doc docDB = this.baseMapper.selectById(id);
            String logId = MDC.get("LOG_ID");
            if(redisUtil.validateRepeat("DOC_VOTE_"+id+"_"+ip,3600*24)){
                wsService.sendInfo("【"+docDB.getName()+"】，被点赞了",logId);
                return this.baseMapper.increaseVoteCount(id);

            }else {
                wsService.sendInfo("【"+docDB.getName()+"】这篇文章您已经点赞了！",logId);
                throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
            }
    }

    @Override
    public int updateEbookInfo() {
        return this.baseMapper.updateEbookInfo();
    }
}





