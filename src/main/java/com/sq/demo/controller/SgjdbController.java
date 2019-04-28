package com.sq.demo.controller;

import com.sq.demo.mapper.SgjdbMapper;
import com.sq.demo.pojo.Sgjdb;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sgjdb")
public class SgjdbController {
    @Autowired
    SgjdbMapper sgjdbMapper;

    @RequestMapping("/getAllSgjdb")
    public List<Sgjdb> getAllSgjdb(){
        List<Sgjdb> res=sgjdbMapper.selectAll();
        for (int i=0;i<res.size();i++){
            res.get(i).setId(String.valueOf(i+1));
        }
        return res;
    }
}
