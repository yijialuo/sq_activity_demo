package com.sq.demo.controller;

import com.sq.demo.mapper.BcwjMapper;
import com.sq.demo.mapper.YscjdwjMapper;
import com.sq.demo.pojo.Bcwj;
import com.sq.demo.pojo.Yscjdwj;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("bcwj")
public class BcwjController {
    @Autowired
    BcwjMapper bcwjMapper;
    @Autowired
    YscjdwjMapper yscjdwjMapper;

    @RequestMapping("insert")
    public String insert(String lc, String jd, String wjmc) {
        Bcwj bcwj = new Bcwj();
        bcwj.setId(IdCreate.id());
        bcwj.setLc(lc);
        bcwj.setJd(jd);
        bcwj.setWjmc(wjmc);
        bcwjMapper.insert(bcwj);
        return bcwj.getId();
    }

    @RequestMapping("delete")
    public boolean delete(String id) {
        return bcwjMapper.deleteByPrimaryKey(id) == 1;
    }

    @RequestMapping("updata")
    public boolean updata(String id, String lc, String jd, String wjmc) {
        Bcwj bcwj=new Bcwj();
        bcwj.setId(id);
        bcwj.setLc(lc);
        bcwj.setJd(jd);
        bcwj.setWjmc(wjmc);
        return bcwjMapper.updateByPrimaryKeySelective(bcwj)==1;
    }

    @RequestMapping("select")
    public List<Bcwj> select(String id,String lc,String jd,String wjmc){
        Bcwj bcwj=new Bcwj();
        bcwj.setId(id);
        bcwj.setLc(lc);
        bcwj.setJd(jd);
        bcwj.setWjmc(wjmc);
        return bcwjMapper.select(bcwj);
    }

    //拿需要上传的文件，参数流程，节点
    @RequestMapping("/bcwjm")
    public List<Bcwj> bcwjm(String lc,String dqjd){
        Bcwj bcwj=new Bcwj();
        bcwj.setLc(lc);
        bcwj.setJd(dqjd);
        return bcwjMapper.select(bcwj);
    }

    //判断文件是否齐全,id为记录id,dqjd为当前节点
    @RequestMapping("/jcwj")
    public boolean jcwj(String id,String lc, String dqjd){
        Bcwj bcwj=new Bcwj();
        bcwj.setLc(lc);
        bcwj.setJd(dqjd);
        //需要传的文件
        List<Bcwj> bcwjs=bcwjMapper.select(bcwj);
        //去找已上传节点文件，是否有
        Yscjdwj yscjdwj=new Yscjdwj();
        for(Bcwj bcwj1:bcwjs){
            yscjdwj.setBcwjid(bcwj1.getId());
            yscjdwj.setJlid(id);
            if(yscjdwjMapper.select(yscjdwj).size()==0){
                return false;
            }
        }
        return true;
    }
}
