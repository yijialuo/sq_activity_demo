package com.sq.demo.controller;

import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/supplier")
public class SupplierController {

    @Autowired
    SupplierMapper supplierMapper;
    @Autowired
    TotalcommentMapper totalcommentMapper;
    @Autowired
    ScoreMapper scoreMapper;

    //供应商名字模糊查询
    @RequestMapping("/gysmcss")
    public List<Supplier> gysmcss(String name){
        return supplierMapper.gysmcss(name);
    }

    //增加供应商
    @RequestMapping("/addSupplier")
    public String addSupplier(@RequestBody Supplier su) {
        su.setId(IdCreate.id());
        supplierMapper.insert(su);
        return su.getId();
    }

    //删除供应商
    @RequestMapping("/deleteSupplier")
    public boolean deleteSupplier(String sid) {
        if (supplierMapper.deleteByPrimaryKey(sid) == 1)
            return true;
        else
            return false;
    }

    //查询供应商
    @RequestMapping("/getAllSuppliers")
    public List<Supplier> getAllSuppliers(){
        return supplierMapper.selectAll();
    }

    //修改供应商
    @RequestMapping("/updateSupplier")
    public boolean updateSupplier(@RequestBody Supplier su){
        if(supplierMapper.updateByPrimaryKeySelective(su)==1){
            return true;
        }else return false;
    }

    //添加总体评价
    @RequestMapping("/addZtpj")
    public String addZtpj(@RequestBody Totalcomment tc){
        tc.setId(IdCreate.id());
        if(totalcommentMapper.insert(tc)==1){
            return tc.getId();
        }else {
            return null;
        }
    }

    //添加评分
    @RequestMapping("/addScore")
    public boolean addScore(@RequestBody Score s){
        s.setId(IdCreate.id());
        if(scoreMapper.insert(s)==1)
            return true;
        else return false;
    }

    //供应商id查所有总体评价
    @RequestMapping("/cxsypj")
    public List<Totalcomment> cxsypj(String sid){
        Totalcomment totalcomment = new Totalcomment();
        totalcomment.setSid(sid);
        List<Totalcomment> totalcomments = totalcommentMapper.select(totalcomment);
        return totalcomments;
    }

    //评价id查具体分数
    @RequestMapping("/cxfs")
    public List<Score> cxfs(String tid){
        Score score = new Score();
        score.setSid(tid);
        List<Score> scores = scoreMapper.select(score);
        Collections.sort(scores, new Comparator<Score>() {
            @Override
            public int compare(Score o1, Score o2) {
                if(Integer.valueOf(o1.getKhnr())>Integer.valueOf(o2.getKhnr()))
                    return 1;
                else if (Integer.valueOf(o1.getKhnr())<Integer.valueOf(o2.getKhnr()))
                    return -1;
                return 0;
            }
        });

        return scores;
    }
}
