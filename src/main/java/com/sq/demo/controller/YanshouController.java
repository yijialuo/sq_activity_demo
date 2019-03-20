package com.sq.demo.controller;

import com.github.pagehelper.PageHelper;

import com.sq.demo.mapper.ContractfileMapper;
import com.sq.demo.mapper.YanshouMapper;
import com.sq.demo.pojo.Contractfile;
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
@RequestMapping("/yanshou")
public class YanshouController {

    @Autowired
    YanshouMapper yanshouMapper;
    @Autowired
    ContractfileMapper contractfileMapper ;

    //归档
    @RequestMapping("/guidang")
    public boolean guidang(String id){
        Yanshou yanshou=new Yanshou();
        yanshou.setId(id);
        yanshou.setGd("1");
        return yanshouMapper.updateByPrimaryKeySelective(yanshou)==1;
    }

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
        Contractfile contractfile=new Contractfile();
        contractfile.setCid(yid);
        //删文件关联表
        contractfileMapper.delete(contractfile);
        if(yanshouMapper.deleteByPrimaryKey(yid)==1){
            return true;
        }else{
            return false;
        }
    }

    //查询所有验收单
    @RequestMapping("/getAllYanshou")
    public List<Yanshou> getAllYanshou(int pageNum){
        PageHelper.startPage(pageNum,10);
        return yanshouMapper.selectAll();
    }

    //数据条数
    @RequestMapping("/AllCounts")
    public int AllCounts(){
        return yanshouMapper.AllCounts();
    }

    //验收编号模糊搜索
    @RequestMapping("/selectYSbyNo")
    List<Yanshou> selectYSbyNo(String ysNo){
        return yanshouMapper.yanshouSSbyNo(ysNo);
    }

    //项目id查询
    @RequestMapping("/selectYSbyprojectid")
    List<Yanshou> selectYSbyprojectid(String projectid){
        Yanshou yanshou=new Yanshou();
        yanshou.setProjectid(projectid);
        return yanshouMapper.select(yanshou);
    }
}
