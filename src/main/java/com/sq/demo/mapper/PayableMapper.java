package com.sq.demo.mapper;

import com.sq.demo.Entity.Payable_Return;
import com.sq.demo.pojo.Payable;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PayableMapper extends MyMapper<Payable> {
    @Select("select * from payable where project=#{pId} order by rq desc limit 1 ")
    Payable selectlatest( String pId);

    List<Payable> search(String projectNo,String projectName,String contractNo,String rq,String jbbm, String jbr,String yszmr,String skdw);
}