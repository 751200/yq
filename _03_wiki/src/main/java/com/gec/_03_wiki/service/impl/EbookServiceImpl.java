package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.exception.BusinessException;
import com.gec._03_wiki.exception.BusinessExceptionCode;
import com.gec._03_wiki.pojo.Ebook;
import com.gec._03_wiki.service.EbookService;
import com.gec._03_wiki.mapper.EbookMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


/**
* @author 751200
* @description 针对表【ebook(电子书)】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook>
    implements EbookService {


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
}




