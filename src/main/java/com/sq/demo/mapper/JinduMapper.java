package com.sq.demo.mapper;

import com.sq.demo.pojo.Jindu;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

import java.awt.*;

public interface JinduMapper extends MyMapper<Jindu> {
    @Select("select * from jindu where project_id=#{project} order by finish_date asc limit 1")
    Jindu selectfirst(@Param("project") String pid);

    @Select("select * from jindu where project_id = #{project} order by finish_date desc")
    List<Jindu> pidcx(@Param("project") String pid);

    //拿已经新建进度了的项目id\
    @Select("SELECT DISTINCT PROJECT_ID FROM jindu")
    List<String> getXmids();
}