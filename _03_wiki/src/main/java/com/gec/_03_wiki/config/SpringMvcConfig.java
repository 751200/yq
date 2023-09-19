package com.gec._03_wiki.config;


import com.gec._03_wiki.utils.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {
// 过滤器的方法打印
@Resource
LoginInterceptor loginInterceptor;

//登录校验，没登陆没数据
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/test/**",
                        "/user/userLogin",
                        "/category/allList",
                        "/ebook/getEbookListByPage",
                        "/doc/allList/**",
                        "/doc/findContentById/**"
                );
    }


}
