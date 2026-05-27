package com.qianyu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Category {
    @TableId(type = IdType.AUTO)
    public int id;
    public String name;
}
