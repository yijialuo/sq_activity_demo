package com.sq.demo.controller;

import com.sq.demo.mapper.JzbMapper;
import com.sq.demo.pojo.Jzb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("jz")
public class JzController {

    @Autowired
    JzbMapper jzbMapper;
    //拿所有可以验收的项目
    @RequestMapping("getAll")
    public List<Jzb> getAll() {
        return jzbMapper.selectAll();
    }

    //添加机种
    @RequestMapping("addJz")
    public boolean addJz(String jz){
        Jzb jzb=new Jzb();
        jzb.setJzmc(jz);
        return jzbMapper.insert(jzb)==1;
    }

    //删除机种
    @RequestMapping("delJz")
    public boolean delJz(String jz){
        Jzb jzb=new Jzb();
        jzb.setJzmc(jz);
        return jzbMapper.delete(jzb)==1;
    }
}
