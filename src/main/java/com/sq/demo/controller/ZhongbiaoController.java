package com.sq.demo.controller;

import com.sq.demo.mapper.ZhaobiaoMapper;
import com.sq.demo.mapper.ZhongbiaoMapper;
import com.sq.demo.pojo.Zhongbiao;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/zhongbiao")
public class ZhongbiaoController {

    @Autowired
    ZhongbiaoMapper zhongbiaoMapper;

    //删除中标
    @RequestMapping("/deletZhongbiao")
    public boolean deletZhongbiao(String id){
        return zhongbiaoMapper.deleteByPrimaryKey(id)==1;
    }

    //添加中标
    @RequestMapping("/addZhongbiao")
    public boolean addZhongbiao(@RequestBody Zhongbiao zhongbiao) {
        zhongbiao.setId(IdCreate.id());
        return zhongbiaoMapper.insert(zhongbiao) == 1;
    }

    //查询中标
    @RequestMapping("/getZhongbiaoByZbid")
    public List<Zhongbiao> getZhongbiaoByZbid(String zbid){
        Zhongbiao zhongbiao=new Zhongbiao();
        zhongbiao.setZbid(zbid);
        return zhongbiaoMapper.select(zhongbiao);
    }
}
