package com.qianyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianyu.bean.Project;
import com.qianyu.service.ProjectService;
import com.qianyu.util.ImageUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qianyu.util.Result;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("project")
public class ProjectController {
    @Autowired
    ProjectService projectService;

    // 图片上传接口（与前端匹配）
    @PostMapping("/upload")
    public Result uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.fail("上传的文件不能为空");
            }
            
            String imagePath = ImageUploadUtil.uploadImage(file);
            return Result.success("图片上传成功", imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("图片上传失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    // 新增：与前端api/upload匹配的上传接口
    @PostMapping("/api/upload")
    public Result uploadImageApi(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.fail("上传的文件不能为空");
            }
            
            String imagePath = ImageUploadUtil.uploadImage(file);
            return Result.success("图片上传成功", imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.fail("图片上传失败：" + e.getMessage());
        } catch (IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/")
    public Result add(@RequestBody Project project){
        projectService.save(project);
        project=projectService.getById(project.getId());
        return Result.success("新增数据成功",project);
    }
    
    @PutMapping("/")
    public Result edit(@RequestBody Project project){
        projectService.updateById(project);
        project=projectService.getById(project.getId());
        return Result.success("编辑数据成功",project);
    }
    
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        projectService.removeById(id);
        return Result.success("删除数据成功");
    }
    
    @GetMapping("/{id}")
    public Result getById(@PathVariable int id){
        Project project=projectService.getById(id);
        return Result.success("",project);
    }
    
    @GetMapping("/")
    public Result getPage(Page page, @RequestParam(value = "searchtext",required = false) String searchtext){
        LambdaQueryWrapper<Project> lambdaQueryWrapper=new LambdaQueryWrapper();
        if(searchtext!=null) {
            lambdaQueryWrapper.like(Project::getName, searchtext);
            lambdaQueryWrapper.or();
            lambdaQueryWrapper.like(Project::getDescr, searchtext);
        }
        page=projectService.page(page,lambdaQueryWrapper);
        return Result.success("",page);
    }
}