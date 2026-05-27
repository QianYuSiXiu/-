package com.qianyu.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {
    
    @Override
    public void insertFill(MetaObject metaObject) {
        // 自动填充创建时间和注册时间
        if (metaObject.hasSetter("registration_date")) {
            metaObject.setValue("registration_date", LocalDateTime.now());
        }
        
        // 自动填充最后登录时间（新增时设置为当前时间）
        if (metaObject.hasSetter("last_login_time")) {
            metaObject.setValue("last_login_time", LocalDateTime.now());
        }
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
        // 更新时自动填充最后登录时间
        if (metaObject.hasSetter("last_login_time")) {
            metaObject.setValue("last_login_time", LocalDateTime.now());
        }
    }
}