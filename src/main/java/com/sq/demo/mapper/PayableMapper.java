package com.sq.demo.mapper;

import com.sq.demo.pojo.Payable;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface PayableMapper extends MyMapper<Payable> {
    @Select("select * from payable where project=#{pId} order by rq desc limit 1 ")
    Payable selectlatest( String pId);
}