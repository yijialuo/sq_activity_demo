package com.sq.demo.mapper;

import com.sq.demo.pojo.Xxmcb;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

public interface XxmcbMapper extends MyMapper<Xxmcb> {

    @Select("select  IFNULL(max(xh),0) from xxmcb where xxmid=#{xxmid} ")
    int getMaxXh(String xxmid);
}