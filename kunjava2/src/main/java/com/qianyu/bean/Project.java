package com.qianyu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Project {
    @TableId(type = IdType.AUTO)
    public  Integer id;
    public  String name;
    public  String img;
    public  String descr;
    public  String specials;
    public  Integer  category_id;

}
