package com.qianyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianyu.bean.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
