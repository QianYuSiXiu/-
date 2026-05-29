package com.qianyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianyu.bean.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * 管理员数据访问层
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}