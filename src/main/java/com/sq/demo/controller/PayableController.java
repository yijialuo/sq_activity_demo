package com.sq.demo.controller;

import com.sq.demo.Entity.Payable_Receive;
import com.sq.demo.Entity.Payable_Return;
import com.sq.demo.mapper.ContractMapper;
import com.sq.demo.mapper.PayableMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Contract;
import com.sq.demo.pojo.Payable;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/payable")
public class PayableController {
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    PayableMapper payableMapper;
    @Autowired
    ProjectMapper projectMapper;

    //添加应付信息
    @RequestMapping("/addPayable")
    public boolean addPayable(@RequestBody Payable_Receive payable_receive){
        Payable payable = new Payable();
        payable_receive.setRq( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        payable.setId(IdCreate.id());
        payable.setJbbm(payable_receive.getJbbm());
        payable.setJbr(payable_receive.getJbr());
        payable.setAccount(payable_receive.getAccount());
        payable.setBank(payable_receive.getBank());
        payable.setBqyf(payable_receive.getBqyf());
        payable.setCwzj(payable_receive.getCwzj());
        payable.setFgld(payable_receive.getFgld());
        payable.setGszjl(payable_receive.getGszjl());
        payable.setInvoiceNo(payable_receive.getInvoice_no());
        payable.setJbbmfzr(payable_receive.getJbbmfzr());
        payable.setProject(payable_receive.getProject());
        payable.setYszmr(payable_receive.getYszmr());
        payable.setZgbmfzr(payable_receive.getZgbmfzr());
        payable.setContractId(payable_receive.getContract_id());
        payable.setRq(payable_receive.getRq());
        Contract contract = new Contract();
        contract.setId(payable_receive.getContract_id());
        contract =contractMapper.selectOne(contract);
        BigDecimal zje = contract.getPrice();
        Payable payable1 = payableMapper.selectlatest(contract.getProjectId());
        BigDecimal yzf;
        BigDecimal wzf;
        if(payable1==null){
            wzf=zje.subtract(payable.getBqyf());
        }else {
            yzf = payable.getBqyf().add((zje.subtract(payable1.getWzf())));
            wzf = zje.subtract(yzf);
        }
        payable.setWzf(wzf);
        if(payableMapper.insert(payable)==1)
            return true;
        else
            return false;
    }

    //合同id查所有支付情况
    @RequestMapping("/htidToPaybles")
    public List<Payable_Return> htidToPaybles(String contractId){
        List<Payable_Return> payable_returns = new ArrayList<>();
        Payable payable = new Payable();
        payable.setContractId(contractId);
        List<Payable> payables = payableMapper.select(payable);
        for(Payable payable1 : payables){
            payable_returns.add(payableTopayable_Return(payable1));
        }
        return payable_returns;
    }

    //项目id查所有支付情况
    @RequestMapping("/pidToPaybles")
    public List<Payable_Return> pidToPaybles(String projectId){
        List<Payable_Return> payable_returns = new ArrayList<>();
        Payable payable = new Payable();
        payable.setProject(projectId);
        List<Payable> payables = payableMapper.select(payable);
        for(Payable payable1 : payables){
            payable_returns.add(payableTopayable_Return(payable1));
        }
        return payable_returns;
    }

    //合同id拿到已支付
    @RequestMapping("/cidToYf")
    public BigDecimal cidToYf(String cid){
        Contract contract = new Contract();
        contract.setId(cid);
        String pid = contractMapper.selectOne(contract).getProjectId();
        BigDecimal zje = contractMapper.selectOne(contract).getPrice();
        Payable payable = payableMapper.selectlatest(pid);
        if(payable==null){
            return new BigDecimal(0);
        }
        BigDecimal wf = payable.getWzf();
        BigDecimal yf = zje.subtract(wf);
        return yf;
    }

    //合同id拿到未支付
    @RequestMapping("/cidToWf")
    public BigDecimal cidToWf(String cid){
        Contract contract = new Contract();
        contract.setId(cid);
        String pid = contractMapper.selectOne(contract).getProjectId();
        BigDecimal zje = contractMapper.selectOne(contract).getPrice();
        Payable payable = payableMapper.selectlatest(pid);
        if(payable==null){
            return zje;
        }
        BigDecimal wf = payable.getWzf();
        return wf;
    }

    //拿所有记录
    @RequestMapping("/getAllPayble")
    public List<Payable_Return> getAllPayble(){
        List<Payable> payables=payableMapper.selectAll();
        List<Payable_Return> res=new ArrayList<>();
        for(Payable payable:payables){
            res.add(payableTopayable_Return(payable));
        }
        return res;
    }

    //payable转payable_return
    public Payable_Return payableTopayable_Return(Payable payable1){
        Payable_Return payable_return=new Payable_Return();
        Contract contract = new Contract();
        contract.setId(payable1.getContractId());
        String contract_no = contractMapper.selectOne(contract).getContractNo();
        BigDecimal htje = contractMapper.selectOne(contract).getPrice();
        String rq = contractMapper.selectOne(contract).getRq();
        String skdw = contractMapper.selectOne(contract).getDfdsr();
        Project project = new Project();
        project.setId(payable1.getProject());
        String project_name = projectMapper.selectOne(project).getProjectNam();
        payable_return.setAccount(payable1.getAccount());
        payable_return.setBank(payable1.getBank());
        payable_return.setBqyf(payable1.getBqyf());
        payable_return.setContract_no(contract_no);
        payable_return.setCwzj(payable1.getCwzj());
        payable_return.setFgld(payable1.getFgld());
        payable_return.setGszjl(payable1.getGszjl());
        payable_return.setHtje(htje);
        payable_return.setHtrq(rq);
        payable_return.setInvoice_no(payable1.getInvoiceNo());
        payable_return.setJbbm(payable1.getJbbm());
        payable_return.setJbbmfzr(payable1.getJbbmfzr());
        payable_return.setJbr(payable1.getJbr());
        payable_return.setProject_name(project_name);
        payable_return.setSkdw(skdw);
        payable_return.setYszmr(payable1.getYszmr());
        payable_return.setZgbmfzr(payable1.getZgbmfzr());
        payable_return.setWfje(payable1.getWzf());
        payable_return.setYzfje(htje.subtract(payable1.getWzf()));
        payable_return.setRq(payable1.getRq());
        payable_return.setId(payable1.getId());
        return payable_return;
    }
}
