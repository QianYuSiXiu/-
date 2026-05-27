package com.qianyu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("project_select")
public class ProjectSelect {
    @TableId(type = IdType.AUTO)
    public Integer id;
    @TableField("user_id")
    public Integer userId;
    @TableField("project_id")
    public Integer projectId;
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public LocalDateTime createTime;
    // getter/setter

    // 非数据库字段
    @TableField(exist = false)
    private Project project;
}
//public class ProjectSelect {
//    @TableId(type = IdType.AUTO)
//    private Integer id;
//    private Integer userId;
//    private Integer projectId;
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    private LocalDateTime createTime;
//}