package com.sq.demo.controller;

import com.sq.demo.Entity.Contract_return;
import com.sq.demo.mapper.ContractMapper;
import com.sq.demo.mapper.ContractfileMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Contract;
import com.sq.demo.pojo.Contractfile;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.IdCreate;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    ContractMapper contractMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ContractfileMapper contractfileMapper;

    //修改合同
    @RequestMapping("/updateContract")
    public boolean updateContract(@RequestBody Contract con){
        if(contractMapper.updateByPrimaryKey(con)==1)
            return true;
        else
            return false;
    }

    //增加合同
    @RequestMapping("/addContract")
    public String addContract(@RequestBody Contract con){
        con.setId(IdCreate.id());
        contractMapper.insert(con);
        return con.getId();
    }

    //删除合同
    @RequestMapping("/deleteContract")
    public boolean deleteContract(String cid){
        Contractfile contractfile=new Contractfile();
        contractfile.setCid(cid);
        //删文件关联表
        contractfileMapper.delete(contractfile);
        if(contractMapper.deleteByPrimaryKey(cid)==1){
            return true;
        }else{
            return false;
        }
    }

    //合同搜索
    @RequestMapping("/contractNoss")
    public List<Contract_return> contractNoss(String contractNo){
        List<Contract> contracts=contractMapper.contractNoss(contractNo);
        List<Contract_return> contract_returns = new ArrayList<>();
        for(Contract contract : contracts){
            contract_returns.add(contractTocontractreturn(contract));
        }
        return contract_returns;
    }

    //查询所有合同
    @RequestMapping("/getAllContracts")
    public List<Contract_return> getAllContracts(){
        List<Contract> contracts = contractMapper.selectAll();
        List<Contract_return> contract_returns = new ArrayList<>();
        for(Contract contract : contracts){
            contract_returns.add(contractTocontractreturn(contract));
        }
        return contract_returns;
    }

    //contract转变为Contract_return
    public Contract_return contractTocontractreturn(Contract contract){
        Project project = new Project();
        project.setId(contract.getProjectId());
        Project p1 = projectMapper.selectOne(project);
        String projectname = p1.getProjectNam();
        Contract_return c = new Contract_return();
        c.setContractNo(contract.getContractNo());
        c.setCwbmyj(contract.getCwbmyj());
        c.setDfdsr(contract.getDfdsr());
        c.setDwyj(contract.getDwyj());
        c.setFgldyj(contract.getFgldyj());
        c.setId(contract.getId());
        c.setJbr(contract.getJbr());
        c.setPrice(contract.getPrice());
        c.setProjectId(contract.getProjectId());
        c.setPsjl(contract.getPsjl());
        c.setTzwh(contract.getTzwh());
        c.setZjlyj(contract.getZjlyj());
        c.setZzsc(contract.getZzsc());
        c.setZbdwyj(contract.getZbdwyj());
        c.setProjectName(projectname);
        return c;
    }

    //拿到附件
    @RequestMapping("/getFjs")
    public List<Contractfile> getFjs(String cid){
        Contractfile contractfile=new Contractfile();
        contractfile.setCid(cid);
        return contractfileMapper.select(contractfile);
    }

    //上传合同附件
    @RequestMapping("/uploadHtfj")
    public boolean uploadHtfj(MultipartFile file, String id)  {
        try {
            ProcessEngine engine= ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService=engine.getRepositoryService();
            InputStream isq=file.getInputStream();
            DeploymentBuilder builder =repositoryService.createDeployment();
            builder.addInputStream(file.getOriginalFilename(),isq);
            Contractfile contractfile=new Contractfile();
            contractfile.setId(IdCreate.id());
            contractfile.setCid(id);
            contractfile.setFname(file.getOriginalFilename());
            contractfile.setFid(builder.deploy().getId());
            contractfileMapper.insert(contractfile);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //点击文件下载/contract/getFj
    @RequestMapping("/getFj")
    public void getFj(HttpServletResponse res, String fid,String fname){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        res.setHeader("Content-Disposition", "attachment;filename=" + fname);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            os = res.getOutputStream();
            bis=new BufferedInputStream(repositoryService.getResourceAsStream(fid,fname));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //删除附件
    @RequestMapping("/deletFj")
    public boolean deletFj(String fid){
        Contractfile contractfile=new Contractfile();
        contractfile.setFid(fid);
        if(contractfileMapper.delete(contractfile)==1)
            return true;
        return false;
    }
}
