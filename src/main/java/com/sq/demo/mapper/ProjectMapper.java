package com.sq.demo.mapper;

import com.sq.demo.Entity.Xm;
import com.sq.demo.pojo.Project;
import com.sq.demo.pojo.Score;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


public interface ProjectMapper extends MyMapper<Project> {
    @Select("select * from project where project_nam like CONCAT('%',#{0},'%')")
    List<Project> xmmcss(String s);
    @Select("select * from project where project_no like CONCAT('%',#{0},'%')")
    List<Project> xmbhss(String s);
    @Select("select * from project limit #{startNo},#{pageSize}")
    List<Project> fenye(@Param("startNo") Integer pageNo, @Param("pageSize") Integer pageSize);
    @Select("select count(*) from project")
    int AllCounts();
    @Select("select * from project order by ENG_TECH_AUDIT_OPINION desc")
    List<Project> selectAll();
    //拿到还可以进行验收的数据
    @Select("SELECT ID,PROJECT_NAM FROM project WHERE ID not in (SELECT PROJECTID FROM yanshou )")
    List<Map> selectYsXm();
    //根据部门名称那项目
    @Select("select * from project where DECLARATION_DEP=#{departmentName} order by ENG_TECH_AUDIT_OPINION desc")
    List<Project> selectByDepartmentName(String departmentName);
}