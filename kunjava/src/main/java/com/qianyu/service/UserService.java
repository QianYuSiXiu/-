package com.qianyu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianyu.bean.User;
import com.qianyu.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    
    // 在新增用户时自动设置注册时间和最后登录时间
    @Override
    public boolean save(User user) {
        if (user.getRegistration_date() == null) {
            user.setRegistration_date(LocalDateTime.now());
        }
        if (user.getLast_login_time() == null) {
            user.setLast_login_time(LocalDateTime.now());
        }
        return super.save(user);
    }
}