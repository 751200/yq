package com.example._02_mybatisplus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@Slf4j
public class MybatisPlusConfiguration {

    public MetaObjectHandler getMetaObjectHandle(){

        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                log.info("start insert fill ...");
                this.setFieldValByName("createTime",new Date(),metaObject);
                this.setFieldValByName("updateTime",new Date(),metaObject);
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                log.info("start update fill...");
                this.setFieldValByName("updateTime",new Date(),metaObject);
            }
        };
    }
}
