package com.sq.demo.controller;

import com.sq.demo.Entity.Xmcxb2;
import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.TaskName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("xmcxb")
public class XmcxbController {
    @Autowired
    XmcxbMapper xmcxbMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    JinduMapper jinduMapper;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    PayableMapper payableMapper;

    public List<Xmcxb> tcsj(List<Xmcxb> res) {
        for (Xmcxb xmcxb : res) {
            Project project = projectMapper.selectByPrimaryKey(xmcxb.getXmid());
            //填充审批状态
            if (project.getPid() == null || project.getPid().equals("")) {
                xmcxb.setSpzt("未申请");
            } else {
                xmcxb.setSpzt(TaskName.pidToTaskName(project.getPid()));
            }
            //填充合同状态
            Contract contract = new Contract();
            contract.setProjectId(project.getId());
            contract = contractMapper.selectOne(contract);
            if (contract == null || contract.getDwyj() == null || contract.getDwyj().equals("")) {
                xmcxb.setHtzt("未申请");
            } else {
                xmcxb.setHtzt(TaskName.pidToTaskName(contract.getDwyj()));
            }
            //填充施工状态
            if (project.getFinishDte() != null && !project.getFinishDte().equals("")) {
                xmcxb.setSgzt("已完工");
            } else {
                Jindu jindu = new Jindu();
                jindu.setProjectId(xmcxb.getXmid());
                xmcxb.setSgzt(jinduMapper.select(jindu).size() == 0 ? "未开工" : "施工进行中");
            }
            //填充结算状态
            Payable payable = new Payable();
            payable.setProject(project.getId());
            List<Payable> payables = payableMapper.select(payable);
            if (payables == null || payables.size() == 0) {
                xmcxb.setJszt("未支付");
            } else {
                boolean t = true;
                for (Payable payable1 : payables) {
                    if (payable1.getWzf() != null && payable1.getWzf().compareTo(new BigDecimal(0.00))==0) {
                        xmcxb.setJszt("支付完成");
                        t = false;
                        break;
                    }
                }
                if (t) {
                    xmcxb.setJszt("支付进行中");
                }
            }
            if (xmcxb.getHtje() != null && !xmcxb.getHtje().equals(""))
                xmcxb.setHtje(xmcxb.getHtje().divide(new BigDecimal(10000)));
            if (xmcxb.getJnjsjd() != null && !xmcxb.getJnjsjd().equals(""))
                xmcxb.setJnjsjd(xmcxb.getJnjsjd().divide(new BigDecimal(10000)));
            if (xmcxb.getZjsjd() != null && !xmcxb.getZjsjd().equals(""))
                xmcxb.setZjsjd(xmcxb.getZjsjd().divide(new BigDecimal(10000)));
        }
        return res;
    }

    @RequestMapping("selectAll")
    public List<Xmcxb> selectAll() {
        return tcsj(xmcxbMapper.selectAll());
    }

    public void Tcjsjs(Xmcxb2 xmcxb2) {
        if (!xmcxb2.getKslxsj().equals("") && xmcxb2.getJslxsj().equals("")) {
            xmcxb2.setJslxsj("9999-99-99");
        }
        if (!xmcxb2.getKskgsj().equals("") && xmcxb2.getJskgsj().equals("")) {
            xmcxb2.setJskgsj("9999-99-99");
        }
        if (!xmcxb2.getKswgsj().equals("") && xmcxb2.getJswgsj().equals("")) {
            xmcxb2.setJswgsj("9999-99-99");
        }
        if (!xmcxb2.getKsxmghsj().equals("") && xmcxb2.getJsxmghsj().equals("")) {
            xmcxb2.setJsxmghsj("9999-99-99");
        }
        if (!xmcxb2.getKsdbsj().equals("") && xmcxb2.getJsdbsj().equals("")) {
            xmcxb2.setJsdbsj("9999-99-99");
        }
        if (!xmcxb2.getKshtqdsj().equals("") && xmcxb2.getJshtqdsj().equals("")) {
            xmcxb2.setJshtqdsj("9999-99-99");
        }
        if (!xmcxb2.getKsjssj().equals("") && xmcxb2.getJsjssj().equals("")) {
            xmcxb2.setJsjssj("9999-99-99");
        }
        if (!xmcxb2.getKslhzbwjsj().equals("") && xmcxb2.getJslhzbwjsj().equals("")) {
            xmcxb2.setJslhzbwjsj("9999-99-99");
        }
    }

    @RequestMapping("select")
    public List<Xmcxb> select(@RequestBody Xmcxb2 xmcxb2) {
        Tcjsjs(xmcxb2);
        List<Xmcxb> res = tcsj(xmcxbMapper.search(xmcxb2));
        //过滤状态
        //过滤审批状态
        if (xmcxb2.getSpzt() != null && !xmcxb2.getSpzt().equals("")) {
            for (int i = 0; i < res.size(); i++) {
                if (!res.get(i).getSpzt().equals(xmcxb2.getSpzt())) {
                    res.remove(i);
                    i--;
                }
            }
        }
        //过滤合同状态
        if (xmcxb2.getHtzt() != null && xmcxb2.getHtzt().length != 0) {
            for (int i = 0; i < res.size(); i++) {
                if (!Arrays.asList(xmcxb2.getHtzt()).contains(res.get(i).getHtzt())) {
                    res.remove(i);
                    i--;
                }
            }
        }
        //过滤施工状态
        if (xmcxb2.getSgzt() != null && xmcxb2.getSgzt().length != 0) {
            for (int i = 0; i < res.size(); i++) {
                if (!Arrays.asList(xmcxb2.getSgzt()).contains(res.get(i).getSgzt())) {
                    res.remove(i);
                    i--;
                }
            }
        }
        //过滤结算状态
        if (xmcxb2.getJszt() != null && xmcxb2.getJszt().length != 0) {
            for (int i = 0; i < res.size(); i++) {
                if (!Arrays.asList(xmcxb2.getJszt()).contains(res.get(i).getJszt())) {
                    res.remove(i);
                    i--;
                }
            }
        }
//        for (int i = 0; i < res.size(); i++) {
//            res.get(i).setXmid(String.valueOf(i + 1));
//        }
        return res;
    }

}
