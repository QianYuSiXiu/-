package com.qianyu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianyu.bean.Admin;
import com.qianyu.mapper.AdminMapper;
import org.springframework.stereotype.Service;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {
    
    // 在新增管理员时自动设置注册时间和最后登录时间
    @Override
    public boolean save(Admin admin) {
        if (admin.getRegistration_date() == null) {
            admin.setRegistration_date(java.time.LocalDateTime.now());
        }
        if (admin.getLast_login_time() == null) {
            admin.setLast_login_time(java.time.LocalDateTime.now());
        }
        return super.save(admin);
    }
}