//package com.gec._03_wiki.config;
//
//
//import com.gec._03_wiki.interceptor.LogInterceptor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class SpringMvcConfig implements WebMvcConfigurer {
//// 过滤器的方法打印
//    @Autowired
//    LogInterceptor logInterceptor;
//
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(logInterceptor)
//                .addPathPatterns("/**");
//    }
//
//
//}
