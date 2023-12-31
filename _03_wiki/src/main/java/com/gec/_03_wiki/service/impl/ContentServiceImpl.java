package com.gec._03_wiki.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gec._03_wiki.pojo.Content;
import com.gec._03_wiki.service.ContentService;
import com.gec._03_wiki.mapper.ContentMapper;
import org.springframework.stereotype.Service;

/**
* @author 751200
* @description 针对表【content】的数据库操作Service实现
* @createDate 2023-09-08 09:50:00
*/
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content>
    implements ContentService{

}




