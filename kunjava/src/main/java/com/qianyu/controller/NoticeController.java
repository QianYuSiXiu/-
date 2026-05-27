package com.qianyu.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qianyu.bean.Notice;
import com.qianyu.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.qianyu.util.Result;

@CrossOrigin
@RestController
@RequestMapping("notice")
public class NoticeController {
    @Autowired
    NoticeService noticeService;

    @PostMapping("/")
    public Result add(@RequestBody Notice notice){
        // 修改这里：调用自定义的 add 方法，而不是 save 方法
        boolean result = noticeService.add(notice);
        if (result) {
            notice = noticeService.getById(notice.getId());
            return Result.success("新增数据成功", notice);
        } else {
            return Result.fail("新增数据失败");
        }
    }
    
    @PutMapping("/")
    public Result edit(@RequestBody Notice notice){
        noticeService.updateById(notice);
        notice=noticeService.getById(notice.getId());
        return Result.success("编辑数据成功",notice);
    }
    
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable int id){
        noticeService.removeById(id);
        return Result.success("删除数据成功");
    }
    
    @GetMapping("/{id}")
    public Result getById(@PathVariable int id){
        Notice notice=noticeService.getById(id);
        return Result.success("",notice);
    }
    
    @GetMapping("/")
    public Result getPage(Page page, @RequestParam(value = "searchtext",required = false) String searchtext){
        LambdaQueryWrapper<Notice> lambdaQueryWrapper=new LambdaQueryWrapper();
        if(searchtext!=null) {
            lambdaQueryWrapper.like(Notice::getTitle, searchtext);
            lambdaQueryWrapper.or();
            lambdaQueryWrapper.like(Notice::getContent, searchtext);
        }
        page=noticeService.page(page,lambdaQueryWrapper);
        return Result.success("",page);
    }
}