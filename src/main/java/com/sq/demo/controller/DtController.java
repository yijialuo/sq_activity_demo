package com.sq.demo.controller;

import com.sq.demo.mapper.DtMapper;
import com.sq.demo.pojo.Dt;
import com.sq.demo.utils.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("dt")
public class DtController {
    @Autowired
    DtMapper dtMapper;
    @Autowired
    UserController userController;

    //增加
    @RequestMapping("insert")
    @Transactional
    public boolean insertDt(@RequestBody Dt dt){
        dt.setCrsj(Time.getNow());
        return dtMapper.insert(dt)==1;
    }

    //删除
    @RequestMapping("delete")
    @Transactional
    public boolean deleteDt(@RequestBody Dt dt){
        return dtMapper.deleteByPrimaryKey(dt.getId())==1;
    }

    //查询
    @RequestMapping("select")
    public List<Dt> selectDt( Dt dt){
        List<Dt> res= dtMapper.select(dt);
        for (Dt dt1:res){
            dt1.setUsername(userController.userIdTouserName(dt1.getUserid()));
            dt1.setDtusername(userController.userIdTouserName(dt1.getDtuserid()));
        }
        return res;
    }
}
