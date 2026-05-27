package com.qianyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianyu.bean.Admin;
import com.qianyu.service.AdminService;
import com.qianyu.util.PasswordUtil;
import com.qianyu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qianyu.util.Result;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    AdminService adminService;
    // 在 AdminController 的 login 方法中修改
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Admin::getAdminname, admin.getAdminname());
        Admin dbAdmin = adminService.getOne(wrapper);

        if (dbAdmin == null) {
            return Result.fail("管理员不存在");
        }

        // 验证密码
        if (!PasswordUtil.matches(admin.getPassword(), dbAdmin.getPassword())) {
            return Result.fail("密码错误");
        }

        // 使用 TokenUtil 生成标准的 JWT token
        String token = TokenUtil.generateAdminToken(String.valueOf(dbAdmin.getId()));

        System.out.println("生成的 JWT token: " + token);
        System.out.println("Token 长度: " + token.length());

        Map<String, Object> data = new HashMap<>();
        data.put("adminId", dbAdmin.getId());
        data.put("adminname", dbAdmin.getAdminname());
        data.put("token", token); // 使用标准的 JWT token

        return Result.success("登录成功", data);
    }

//    @PostMapping("/login")
//    public Result login(@RequestBody Admin admin) {
//        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Admin::getAdminname, admin.getAdminname());
//        Admin dbAdmin = adminService.getOne(wrapper);
//
//        if (dbAdmin == null) {
//            return Result.fail("管理员不存在");
//        }
//
//        // 调试信息：打印密码对比
//        System.out.println("输入的用户名: " + admin.getAdminname());
//        System.out.println("输入的密码: " + admin.getPassword());
//        System.out.println("数据库中的密码: " + dbAdmin.getPassword());
//        System.out.println("加密后的输入密码: " + PasswordUtil.encodePassword(admin.getPassword()));
//
//        // 尝试加密验证
//        boolean encryptedMatch = PasswordUtil.matches(admin.getPassword(), dbAdmin.getPassword());
//        System.out.println("加密验证结果: " + encryptedMatch);
//
//        // 如果加密验证失败，尝试明文验证（临时解决方案）
//        boolean plainMatch = admin.getPassword().equals(dbAdmin.getPassword());
//        System.out.println("明文验证结果: " + plainMatch);
//
//        if (!encryptedMatch && !plainMatch) {
//            return Result.fail("密码错误");
//        }
//
//        // 如果数据库中是明文，自动更新为加密密码
//        if (plainMatch && !encryptedMatch) {
//            System.out.println("检测到明文密码，自动更新为加密格式...");
//            dbAdmin.setPassword(PasswordUtil.encodePassword(admin.getPassword()));
//            adminService.updateById(dbAdmin);
//        }
//
//        // 模拟生成Token
//        Map<String, Object> data = new HashMap<>();
//        data.put("adminId", dbAdmin.getId());
//        data.put("adminname", dbAdmin.getAdminname());
//        data.put("token", "admin_" + System.currentTimeMillis());
//
//        return Result.success("登录成功", data);
//    }

    // 新增：管理员登录接口
//    @PostMapping("/login")
//    public Result login(@RequestBody Admin admin) {
//        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(Admin::getAdminname, admin.getAdminname());
//        Admin dbAdmin = adminService.getOne(wrapper);
//        if (dbAdmin == null) {
//            return Result.fail("管理员不存在");
//        }
//        // 验证密码（加密后对比）
//        if (!PasswordUtil.matches(admin.getPassword(), dbAdmin.getPassword())) {
//            return Result.fail("密码错误");
//        }
//        // 模拟生成Token（实际项目建议用JWT）
//        Map<String, Object> data = new HashMap<>();
//        data.put("adminId", dbAdmin.getId());
//        data.put("adminname", dbAdmin.getAdminname());
//        data.put("token", "admin_" + System.currentTimeMillis()); // 临时Token
//        return Result.success("登录成功", data);
//    }

    // 原有CRUD方法保持不变
    @PostMapping("/")
    public Result add(@RequestBody Admin admin){
        admin.setPassword(PasswordUtil.encodePassword(PasswordUtil.DEFAULT_PASSWORD));
        adminService.save(admin);
        admin=adminService.getById(admin.getId());
        return Result.success("新增数据成功",admin);
    }

    @PutMapping("/")
    public Result edit(@RequestBody Admin admin){
        adminService.updateById(admin);
        admin=adminService.getById(admin.getId());
        return Result.success("编辑数据成功",admin);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        adminService.removeById(id);
        return Result.success("删除数据成功");
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id){
        Admin admin=adminService.getById(id);
        return Result.success("",admin);
    }

    @GetMapping("/")
    public Result getPage(Page page, @RequestParam(value = "searchtext",required = false) String searchtext){
        LambdaQueryWrapper<Admin> lambdaQueryWrapper=new LambdaQueryWrapper();
        if(searchtext!=null) {
            lambdaQueryWrapper.like(Admin::getAdminname, searchtext);
            lambdaQueryWrapper.or();
            lambdaQueryWrapper.like(Admin::getRealname, searchtext);
        }
        page=adminService.page(page,lambdaQueryWrapper);
        return Result.success("",page);
    }
    @PutMapping("/password")
    public Result changePassword(@RequestBody Map<String, Object> params) {
        Integer adminId = (Integer) params.get("adminId");
        String oldPwd = (String) params.get("oldPassword");
        String newPwd = (String) params.get("newPassword");

        Admin admin = adminService.getById(adminId);
        if (admin == null) {
            return Result.fail("管理员不存在");
        }
        // 验证旧密码
        if (!PasswordUtil.matches(oldPwd, admin.getPassword())) {
            return Result.fail("旧密码错误");
        }
        // 加密新密码并更新
        admin.setPassword(PasswordUtil.encodePassword(newPwd));
        adminService.updateById(admin);
        return Result.success("密码修改成功");
    }
}