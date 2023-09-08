package com.gec._03_wiki.mapper;

import com.gec._03_wiki.pojo.Doc;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 751200
* @description 针对表【doc】的数据库操作Mapper
* @createDate 2023-09-08 09:50:00
* @Entity com.gec._03_wiki.pojo.Doc
*/
@Mapper
public interface DocMapper extends BaseMapper<Doc> {

}




