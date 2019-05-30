package com.sq.demo.mapper;

import com.sq.demo.pojo.Yanshou;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface YanshouMapper extends MyMapper<Yanshou> {
    //供应商名称查询
    @Select("select * from yanshou where YSNO like CONCAT('%',#{0},'%')")
    List<Yanshou> yanshouSSbyNo(String s);

    //projectId查询
    @Select("select * from yanshou order by cjsj desc")
    List<Yanshou> selectAll();

    @Select("select count(*) from yanshou")
    int AllCounts();

    List<Yanshou> search(String ysNo,String kgrq,String sjjgrq,String ysrq);

    @Select("select DISTINCT PROJECTID from yanshou ")
    List<String> getXmids();
}