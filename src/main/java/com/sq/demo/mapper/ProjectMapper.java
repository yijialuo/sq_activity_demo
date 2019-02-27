package com.sq.demo.mapper;

import com.sq.demo.pojo.Project;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;



public interface ProjectMapper extends MyMapper<Project> {
    @Select("select * from project where project_nam like CONCAT('%',#{0},'%')")
    List<Project> xmmcss(String s);
    @Select("select * from project where project_no like CONCAT('%',#{0},'%')")
    List<Project> xmbhss(String s);
    @Select("select * from project limit #{startNo},#{pageSize}")
    List<Project> fenye(@Param("startNo") Integer pageNo, @Param("pageSize") Integer pageSize);
}