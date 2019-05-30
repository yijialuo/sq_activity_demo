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

    //拿到所有还没付款完的合同id
    @Select("SELECT DISTINCT CONTRACT_ID FROM payable WHERE CONTRACT_ID  not in (  SELECT CONTRACT_ID FROM payable WHERE WZF=0 )")
    List<String> getCanpayHtid();

    //拿到所有付款完了的
    @Select("select distinct CONTRACT_ID from payable where wzf=0")
    List<String> getFwHtid();
}