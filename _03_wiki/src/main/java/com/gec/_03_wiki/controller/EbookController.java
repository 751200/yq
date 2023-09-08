package com.gec._03_wiki.controller;

import com.gec._03_wiki.mapper.EbookMapper;
import com.gec._03_wiki.pojo.Ebook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EbookController {

    @Autowired
    private  EbookMapper ebookmapper;

    @GetMapping("/ebookAll")
    public List<Ebook> getAll(){
        List<Ebook> list = ebookmapper.selectList(null);
        return list;
    }
}
