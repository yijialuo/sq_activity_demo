package com.sq.demo.controller;

import com.sq.demo.mapper.XxmcbMapper;
import com.sq.demo.mapper.XxmglMapper;
import com.sq.demo.pojo.Xxmcb;
import com.sq.demo.pojo.Xxmgl;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.Time;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/xxmgl")
public class XxmglController {

    @Autowired
    XxmglMapper xxmglMapper;
    @Autowired
    XxmcbMapper xxmcbMapper;

    @Transactional
    @RequestMapping("/insert")
    public boolean insert(String xmbh, String xmmc, String lxbm, String sqr) {
        try {
            Xxmgl xxmgl = new Xxmgl();
            xxmgl.setId(IdCreate.id());
            xxmgl.setCjsj(Time.getNow());
            xxmgl.setXmbh(xmbh);
            xxmgl.setXmmc(xmmc);
            xxmgl.setLxbm(lxbm);
            xxmgl.setSqr(sqr);
            xxmglMapper.insert(xxmgl);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    @RequestMapping("/delete")
    public boolean delete(String id) {
        try {
            //删主表
            xxmglMapper.deleteByPrimaryKey(id);
            //删从表
            Xxmcb xxmcb=new Xxmcb();
            xxmcb.setXxmid(id);
            xxmcbMapper.delete(xxmcb);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Transactional
    @RequestMapping("/updata")
    public boolean update(String id, String xmbh, String xmmc, String lxbm, String sqr) {
        Xxmgl xxmgl = new Xxmgl();
        xxmgl.setId(id);
        if (xmbh != null && !xmbh.equals(""))
            xxmgl.setXmbh(xmbh);
        if (xmmc != null && !xmmc.equals(""))
            xxmgl.setXmmc(xmmc);
        if (lxbm != null && !lxbm.equals(""))
            xxmgl.setLxbm(lxbm);
        if (sqr != null && !sqr.equals(""))
            xxmgl.setSqr(sqr);
        return xxmglMapper.updateByPrimaryKeySelective(xxmgl) == 1 ? true : false;
    }

    @RequestMapping("/select")
    public List<Xxmgl> select(String xmbh, String xmmc, String lxbm, String sqr) {
        Xxmgl xxmgl = new Xxmgl();
        if (xmbh != null && !xmbh.equals(""))
            xxmgl.setXmbh(xmbh);
        if (xmmc != null && !xmmc.equals(""))
            xxmgl.setXmmc(xmmc);
        if (lxbm != null && !lxbm.equals(""))
            xxmgl.setLxbm(lxbm);
        if (sqr != null && !sqr.equals(""))
            xxmgl.setSqr(sqr);
        return xxmglMapper.select(xxmgl);
    }

    @RequestMapping("/selectAll")
    public List<Xxmgl> selectAll() {
        return xxmglMapper.selectAll();
    }
}
