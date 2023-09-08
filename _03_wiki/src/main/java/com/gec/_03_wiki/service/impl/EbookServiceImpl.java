package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.pojo.Ebook;
import com.gec._03_wiki.service.EbookService;
import com.gec._03_wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

/**
* @author 751200
* @description 针对表【ebook(电子书)】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook>
    implements EbookService{

}




