package com.sq.demo.mapper;

import com.sq.demo.pojo.Supplier;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SupplierMapper extends MyMapper<Supplier> {
    //供应商名称查询
    @Select("select * from supplier where name like CONCAT('%',#{0},'%')")
    List<Supplier> gysmcss(String s);
}