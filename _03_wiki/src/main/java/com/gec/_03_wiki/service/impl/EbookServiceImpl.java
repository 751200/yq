package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.exception.BusinessException;
import com.gec._03_wiki.exception.BusinessExceptionCode;
import com.gec._03_wiki.pojo.Ebook;
import com.gec._03_wiki.pojo.req.EbookQueryReq;
import com.gec._03_wiki.pojo.resp.EbookQueryResp;
import com.gec._03_wiki.pojo.resp.PageResp;
import com.gec._03_wiki.service.EbookService;
import com.gec._03_wiki.mapper.EbookMapper;
import com.gec._03_wiki.utils.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;


/**
* @author 751200
* @description 针对表【ebook(电子书)】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook>
    implements EbookService {

    @Autowired
    private static EbookMapper ebookMapper;


    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public String uploadImage(MultipartFile file,String folder){
        if(file == null){
            throw new BusinessException(BusinessExceptionCode.Select_Upload_Image);
        }
        if(file.getSize() > 1024*1024*10){
            throw new BusinessException(BusinessExceptionCode.Files_Larger_Than_10M);
        }

        String suffix = file.getOriginalFilename()
                .substring(file.getOriginalFilename().lastIndexOf(".") + 1,file.getOriginalFilename().length());

        if(!"jpg,jpeg,gif,png".toUpperCase().contains(suffix.toUpperCase())){
            throw new BusinessException(BusinessExceptionCode.Files_Wrong_Format);
        }

        String savePath = folder;
        File savePathFile = new File(savePath);
        if(!savePathFile.exists()){
            savePathFile.mkdir();
        }

        String filename = UUID.randomUUID().toString().replaceAll("-","") + "." + suffix;

        try{
            file.transferTo(new File(savePath +filename));
            File file1 = new File(file.getOriginalFilename());
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(BusinessExceptionCode.Save_File_Exception);
        }

        LOG.info("文件名:{}",filename);
        return  filename;
    }

    @Override
    public PageResp<EbookQueryResp> getEbookListByPage(EbookQueryReq req) {
        Page<Ebook> page = new Page<>(req.getPage(),req.getSize());
        QueryWrapper<Ebook> wrapper= new QueryWrapper<>();
        if(req.getCategory2Id()!=0)
            wrapper.eq("category2_id",req.getCategory2Id());
        if(req.getName()!=null)
            wrapper.like("name",req.getName());
        page = this.page(page,wrapper);
        List<Ebook> list = page.getRecords();

        LOG.info("总行数：{}",page.getTotal()+"");
        LOG.info("总页数：{}",page.getPages()+"");

        List<EbookQueryResp> ebookQueryResps = CopyUtil.copyList(list, EbookQueryResp.class);

        PageResp<EbookQueryResp> respPage = new PageResp<>();
        respPage.setTotal(page.getTotal());
        respPage.setList(ebookQueryResps);
        return respPage;
    }
}




