package com.sq.demo.mapper;

import com.sq.demo.pojo.Contract;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContractMapper extends MyMapper<Contract> {
    @Select("select * from contract where CONTRACT_NO like CONCAT('%',#{0},'%')")
    List<Contract> contractNoss(String s);
}