package com.qianyu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianyu.bean.Notice;
import com.qianyu.mapper.NoticeMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {
    // 定义时间格式化器
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 新增公告 - 自动设置当前时间
     */
    public boolean add(Notice notice) {
        log.info("=== 开始添加公告 ===");
        log.info("接收到的公告: title={}, content={}", notice.getTitle(), notice.getContent());
        log.info("原始时间: {}", notice.getTime());

        // 强制设置当前时间
        String currentTime = LocalDateTime.now().format(formatter);
        notice.setTime(currentTime);

        log.info("设置后的时间: {}", notice.getTime());

        // 保存到数据库
        boolean result = this.save(notice);

        log.info("保存结果: {}, 生成的ID: {}", result, notice.getId());
        log.info("=== 添加公告结束 ===");

        return result;
    }

    // 重写 save 方法，确保记录保存日志
    @Override
    public boolean save(Notice entity) {
        log.info("调用 save 方法，实体内容: {}", entity);
        log.info("save 方法中的 time 字段值: {}", entity.getTime());
        return super.save(entity);
    }
}