package com.sq.demo.mapper;

import com.sq.demo.pojo.Zhaobiao;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ZhaobiaoMapper extends MyMapper<Zhaobiao> {
    @Select("select count(*) from zhaobiao")
    int AllCounts();
    @Select("select * from zhaobiao order by cjsj desc")
    List<Zhaobiao> selectAll();
}