package com.qianyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianyu.bean.ProjectSelect;
import com.qianyu.service.ProjectSelectService;
import com.qianyu.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qianyu.util.Result;
import jakarta.servlet.http.HttpServletRequest;

@CrossOrigin
@RestController
@RequestMapping("projectselect")
public class ProjectSelectController {
    @Autowired
    ProjectSelectService projectSelectService;

    // 1. 用户选取课题（新增记录）
    @PostMapping("/")
    public Result selectProject(@RequestBody ProjectSelect projectSelect, HttpServletRequest request) {
        try {
            // 获取当前登录用户ID
            int userId = TokenUtil.getLoginUserID(request);
            projectSelect.setUserId(userId);
            
            // 检查是否已经选取过该课题
            LambdaQueryWrapper<ProjectSelect> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProjectSelect::getUserId, userId)
                   .eq(ProjectSelect::getProjectId, projectSelect.getProjectId());
            if (projectSelectService.getOne(wrapper) != null) {
                return Result.fail("您已经选取过该课题");
            }
            
            projectSelectService.save(projectSelect);
            return Result.success("课题选取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("课题选取失败：" + e.getMessage());
        }
    }

    // 2. 用户取消选取（删除记录）
    @DeleteMapping("/{projectId}")
    public Result cancelSelect(@PathVariable int projectId, HttpServletRequest request) {
        try {
            // 获取当前登录用户ID
            int userId = TokenUtil.getLoginUserID(request);
            
            // 删除选取记录
            LambdaQueryWrapper<ProjectSelect> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProjectSelect::getUserId, userId)
                   .eq(ProjectSelect::getProjectId, projectId);
            projectSelectService.remove(wrapper);
            
            return Result.success("课题取消选取成功");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("取消选取失败：" + e.getMessage());
        }
    }

    // 3. 查询当前用户已选课题
    @GetMapping("/")
    public Result getSelectedProjects(HttpServletRequest request) {
        try {
            // 获取当前登录用户ID
            int userId = TokenUtil.getLoginUserID(request);
            
            // 查询用户已选课题
            LambdaQueryWrapper<ProjectSelect> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ProjectSelect::getUserId, userId);
            
            // 如果需要分页，可以添加分页参数
            Page<ProjectSelect> page = new Page<>(1, 10); // 默认第1页，每页10条
            page = projectSelectService.page(page, wrapper);
            
            return Result.success("获取成功", page);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("获取失败：" + e.getMessage());
        }
    }

    // 4. 管理员查询所有用户选取情况
    @GetMapping("/all")
    public Result getAllSelectedProjects(Page page) {
        try {
            // 管理员查询所有用户选取情况
            page = projectSelectService.page(page);
            return Result.success("获取成功", page);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("获取失败：" + e.getMessage());
        }
    }
}