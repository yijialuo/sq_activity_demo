package com.sq.demo.mapper;

import com.sq.demo.pojo.Contract;
import com.sq.demo.utils.MyMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ContractMapper extends MyMapper<Contract> {
    @Select("select * from contract where CONTRACT_NO like CONCAT('%',#{0},'%')")
    List<Contract> contractNoss(String s);
    @Select("select * from contract order by cjsj desc")
    List<Contract> selectAll();
    @Select("select count(*) from contract")
    int AllCounts();
    @Select("SELECT * FROM contract WHERE dwyj is not null and dwyj != ''")
    List<Contract> selectYlc();

    List<Contract> search(String contractNo,String ContractDate,String dfdsr,String tzwh);
}