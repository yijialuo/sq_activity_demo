package com.sq.demo.controller;


import com.sq.demo.mapper.ZjhfzMapper;
import com.sq.demo.pojo.Zjhfz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("zjhfz")
public class ZjlfzController {

    @Autowired
    ZjhfzMapper zjhfzMapper;

    @RequestMapping("updata")
    public boolean updata(String fz){
        Zjhfz zjhfz=new Zjhfz();
        zjhfz.setId("1");
        zjhfz.setFz(fz);
        return zjhfzMapper.updateByPrimaryKeySelective(zjhfz)==1;
    }

    @RequestMapping("get")
    public List<Zjhfz> get(){
        return zjhfzMapper.selectAll();
    }
}
