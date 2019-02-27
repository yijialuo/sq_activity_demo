package com.sq.demo.controller;

import com.sq.demo.mapper.YanshouMapper;
import com.sq.demo.pojo.Yanshou;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/contract")
public class YanshouController {

    @Autowired
    YanshouMapper yanshouMapper;

    //增加验收单
    @RequestMapping("/addYanshou")
    public String addYanshou(@RequestBody Yanshou ys){
        ys.setId(IdCreate.id());
        yanshouMapper.insert(ys);
        return ys.getId();
    }

    //修改验收单
    @RequestMapping("/updateYanshou")
    public boolean updateYanshou(@RequestBody Yanshou ys){
        if(yanshouMapper.updateByPrimaryKeySelective(ys)==1){
            return true;
        }else {
            return false;
        }
    }

    //删除验收单
    @RequestMapping("/deleteYanshou")
    public boolean deleteYanshou(String yid){
        if(yanshouMapper.deleteByPrimaryKey(yid)==1){
            return true;
        }else{
            return false;
        }
    }

    //查询所有验收单
    @RequestMapping("/getAllYanshou")
    public List<Yanshou> getAllYanshou(){
        return yanshouMapper.selectAll();
    }
}
