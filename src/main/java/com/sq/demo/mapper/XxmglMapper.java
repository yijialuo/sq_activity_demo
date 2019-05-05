package com.sq.demo.mapper;

import com.sq.demo.pojo.Xxmgl;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface XxmglMapper extends MyMapper<Xxmgl> {

    @Select("select y1 from xxmgl")
    List<String> getAllXmid();

    @Select("select * from xxmgl where lxbm=#{departmentName}")
    List<Xxmgl> getSelfXxmgl(String departmentName);
}