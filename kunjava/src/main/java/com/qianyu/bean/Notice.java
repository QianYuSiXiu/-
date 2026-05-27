package com.qianyu.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notice {
    @TableId(type = IdType.AUTO)
    public Integer id;
    public String title;
    public String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 用于返回给前端时的格式化
    public String time;

}
