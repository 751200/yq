package com.gec._03_wiki.service;

import com.gec._03_wiki.pojo.Ebook;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
* @author 751200
* @description 针对表【ebook(电子书)】的数据库操作Service
* @createDate 2023-09-08 09:50:00
*/
public interface EbookService extends IService<Ebook> {

    String uploadImage(MultipartFile file, String s);
}
