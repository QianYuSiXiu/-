package com.qianyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianyu.bean.User;
import com.qianyu.service.UserService;
import com.qianyu.util.PasswordUtil;
import com.qianyu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qianyu.util.Result;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    UserService userService;

    // 新增：修改密码接口
    @PutMapping("/password")
    public Result changePassword(@RequestBody Map<String, Object> params) {
        Integer userId = (Integer) params.get("userId");
        String oldPwd = (String) params.get("oldPassword");
        String newPwd = (String) params.get("newPassword");

        User user = userService.getById(userId);
        if (user == null) {
            return Result.fail("用户不存在");
        }
        // 验证旧密码
        if (!PasswordUtil.matches(oldPwd, user.getPassword())) {
            return Result.fail("旧密码错误");
        }
        // 加密新密码并更新
        user.setPassword(PasswordUtil.encodePassword(newPwd));
        userService.updateById(user);
        return Result.success("密码修改成功");
    }

    // 原有CRUD方法保持不变
    @PostMapping("/")
    public Result add(@RequestBody User user){
        user.setPassword(PasswordUtil.encodePassword(user.getPassword()));
        userService.save(user);
        user=userService.getById(user.getId());
        return Result.success("新增数据成功",user);
    }

    @PutMapping("/")
    public Result edit(@RequestBody User user){
        userService.updateById(user);
        user=userService.getById(user.getId());
        return Result.success("编辑数据成功",user);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        userService.removeById(id);
        return Result.success("删除数据成功");
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable int id){
        User user=userService.getById(id);
        return Result.success("",user);
    }

    @GetMapping("/")
    public Result getPage(Page page, @RequestParam(value = "searchtext",required = false) String searchtext){
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper();
        if(searchtext!=null) {
            lambdaQueryWrapper.like(User::getUsername, searchtext);
            lambdaQueryWrapper.or();
            lambdaQueryWrapper.like(User::getNickname, searchtext);
        }
        page=userService.page(page,lambdaQueryWrapper);
        return Result.success("",page);
    }
    @PostMapping("/login")
    public Result login(@RequestBody User user) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, user.getUsername());
        User dbUser = userService.getOne(wrapper);
        if (dbUser == null) {
            return Result.fail("用户不存在");
        }
        if (!PasswordUtil.matches(user.getPassword(), dbUser.getPassword())) {
            return Result.fail("密码错误");
        }

        // 生成 JWT
        String token = TokenUtil.generateUserToken(String.valueOf(dbUser.getId()));

        // 添加打印语句
        System.out.println("=== 用户登录成功 ===");
        System.out.println("用户ID: " + dbUser.getId());
        System.out.println("用户名: " + dbUser.getUsername());
        System.out.println("生成的Token: " + token);
        System.out.println("Token长度: " + token.length());
        System.out.println("Token前50位: " + (token.length() > 50 ? token.substring(0, 50) + "..." : token));

        Map<String, Object> data = new HashMap<>();
        data.put("userId", dbUser.getId());
        data.put("username", dbUser.getUsername());
        data.put("token", token);
        return Result.success("登录成功", data);
    }

//    @PostMapping("/login")
//    public Result login(@RequestBody User user) {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        User dbUser = userService.getOne(wrapper);
//        if (dbUser == null) {
//            return Result.fail("用户不存在");
//        }
//        if (!PasswordUtil.matches(user.getPassword(), dbUser.getPassword())) {
//            return Result.fail("密码错误");
//        }
//        // 用TokenUtil生成JWT
//        String token = TokenUtil.generateUserToken(String.valueOf(dbUser.getId()));
//        Map<String, Object> data = new HashMap<>();
//        data.put("userId", dbUser.getId());
//        data.put("username", dbUser.getUsername());
//        data.put("token", token);
//        return Result.success("登录成功", data);
//    }


//    @PostMapping("/login")
//    public Result login(@RequestBody User user) {
//        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(User::getUsername, user.getUsername());
//        User dbUser = userService.getOne(wrapper);
//        if (dbUser == null) {
//            return Result.fail("用户不存在");
//        }
//        // 验证密码（加密后对比）
//        if (!PasswordUtil.matches(user.getPassword(), dbUser.getPassword())) {
//            return Result.fail("密码错误");
//        }
//        // 生成Token（可用JWT等）
//        Map<String, Object> data = new HashMap<>();
//        data.put("userId", dbUser.getId());
//        data.put("username", dbUser.getUsername());
//        data.put("token", "user_" + System.currentTimeMillis()); // 临时Token
//        return Result.success("登录成功", data);
//    }
}