package com.qianyu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianyu.bean.Category;
import com.qianyu.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {
}
