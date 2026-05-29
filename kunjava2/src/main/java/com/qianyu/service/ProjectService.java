package com.qianyu.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qianyu.bean.Project;
import com.qianyu.mapper.ProjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ProjectService extends ServiceImpl<ProjectMapper, Project> {
}
