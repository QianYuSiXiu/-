package com.qianyu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qianyu.bean.Project;
import com.qianyu.bean.ProjectSelect;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

@Mapper
public interface ProjectSelectMapper extends BaseMapper<ProjectSelect> {
    // 查询当前用户所有选取课题（带课题详情）
    @Select("SELECT ps.*, p.id AS p_id, p.name, p.specials " +
            "FROM project_select ps " +
            "LEFT JOIN project p ON ps.project_id = p.id " +
            "WHERE ps.user_id = #{userId}")
    @Results({
            @Result(column = "id", property = "id"),
            @Result(column = "user_id", property = "userId"),
            @Result(column = "project_id", property = "projectId"),
            @Result(column = "create_time", property = "createTime"),
            @Result(property = "project", column = "p_id", javaType = Project.class,
                    one = @One(select = "com.qianyu.mapper.ProjectMapper.selectById", fetchType = FetchType.DEFAULT))
    })
    List<ProjectSelect> selectWithProjectByUserId(@Param("userId") Integer userId);
}