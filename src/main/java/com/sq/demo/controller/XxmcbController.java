package com.sq.demo.controller;

import com.sq.demo.mapper.XxmcbMapper;
import com.sq.demo.pojo.Xxmcb;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/xxmcb")
public class XxmcbController {
    @Autowired
    XxmcbMapper xxmcbMapper;

    //插入
    @Transactional
    @RequestMapping("/insert")
    public boolean insert(String xxmid, String xh, String gzbzmc, String sgdw, String htje, String htqdsj, String bz) {
        Xxmcb xxmcb = new Xxmcb();
        xxmcb.setId(IdCreate.id());
        xxmcb.setXxmid(xxmid);
        xxmcb.setXh(xh);
        xxmcb.setGzbzmc(gzbzmc);
        xxmcb.setSgdw(sgdw);
        xxmcb.setHtje(new BigDecimal(htje));
        xxmcb.setHtqdsj(htqdsj);
        xxmcb.setBz(bz);
        return xxmcbMapper.insert(xxmcb) == 1;
    }

    @Transactional
    @RequestMapping("/delete")
    public boolean delete(String id) {
        return xxmcbMapper.deleteByPrimaryKey(id) == 1;
    }

    @Transactional
    @RequestMapping("/updata")
    public boolean updata(String id, String xh, String gzbzmc, String sgdw, String htje, String htqdsj, String bz) {
        Xxmcb xxmcb = new Xxmcb();
        xxmcb.setId(id);
        xxmcb.setXh(xh);
        xxmcb.setGzbzmc(gzbzmc);
        xxmcb.setSgdw(sgdw);
        xxmcb.setHtje(new BigDecimal(htje));
        xxmcb.setHtqdsj(htqdsj);
        xxmcb.setBz(bz);
        return xxmcbMapper.updateByPrimaryKeySelective(xxmcb) == 1;
    }


    @RequestMapping("/select")
    public List<Xxmcb> select(String id, String xxmid, String xh, String gzbzmc, String sgdw, String htje, String bz) {
        Xxmcb xxmcb = new Xxmcb();
        if (id != null && !id.equals("")) {
            xxmcb.setId(id);
        }
        if (xxmid != null && !xxmid.equals("")) {
            xxmcb.setXxmid(xxmid);
        }
        if (xh != null && !xh.equals("")) {
            xxmcb.setXh(xh);
        }
        if (gzbzmc != null && !gzbzmc.equals("")) {
            xxmcb.setGzbzmc(gzbzmc);
        }
        if (sgdw != null && !sgdw.equals("")) {
            xxmcb.setSgdw(sgdw);
        }
        if (htje != null && !htje.equals("")) {
            xxmcb.setHtje(new BigDecimal(htje));
        }
        if (bz != null && !bz.equals("")) {
            xxmcb.setBz(bz);
        }
        return xxmcbMapper.select(xxmcb);
    }
}
