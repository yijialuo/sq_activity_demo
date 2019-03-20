package com.sq.demo.controller;

import com.github.pagehelper.PageHelper;
import com.sq.demo.Entity.Contract_return;
import com.sq.demo.Entity.Hetong;
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

    //归档
    @RequestMapping("/guidang")
    public boolean guidang(String id){
        Contract contract=new Contract();
        contract.setId(id);
        contract.setGd("1");
        return contractMapper.updateByPrimaryKeySelective(contract)==1;
    }

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

    //合同id搜索
    @RequestMapping("/xmIdSS")
    public Contract_return xmIdSS(String xmId){
        Contract contract=new Contract();
        contract.setProjectId(xmId);
        contract=contractMapper.selectOne(contract);
        if(contract==null)
            return null;
        return contractTocontractreturn(contract);
    }

    //查询总数
    @RequestMapping("/AllCounts")
    public int AllCounts(){
        return contractMapper.AllCounts();
    }

    //查询所有合同
    @RequestMapping("/getAllContracts")
    public List<Contract_return> getAllContracts(int pageNum){
        PageHelper.startPage(pageNum,10);
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
        c.setRq(contract.getRq());
        c.setProjectName(projectname);
        c.setGd(contract.getGd());
        return c;
    }

    //拿到日期
    @RequestMapping("/getRq")
    public String getRq(String contractId){
        Contract contract=new Contract();
        contract.setId(contractId);
        return contractMapper.selectOne(contract).getRq();
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
    public void getFj(HttpServletResponse res, String fid,String fname) throws UnsupportedEncodingException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        res.setHeader("content-type", "application/octet-stream");
        res.setContentType("application/octet-stream");
        //中文文件名不行，需要转码
        String file_name = new String(fname.getBytes(), "ISO-8859-1");
        res.setHeader("Content-Disposition", "attachment;filename=" + file_name);
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

    //拿到所有合同id和合同编号
    @RequestMapping("/getAllHtidAndHtno")
    public List<Hetong> getallhtid(){
        List<Contract> contracts = contractMapper.selectAll();
        List<Hetong> hetongs = new ArrayList<>();
        for (Contract contract : contracts) {
            Hetong hetong = new Hetong();
            hetong.value = contract.getId();
            hetong.label = contract.getContractNo();
            hetongs.add(hetong);
        }
        return hetongs;
    }

    //查询合同
    @RequestMapping("/selectContract")
    public Contract selectContract(String contractId){
        return contractMapper.selectByPrimaryKey(contractId);
    }

    //根据合同id拿到对方当事人
    @RequestMapping("/getDfdsr")
    public String getDfdsr(String contractId){
        Contract contract = new Contract();
        contract.setId(contractId);
        String name = contractMapper.selectOne(contract).getDfdsr();
        return name;
    }

    //根据合同id拿到项目名称
    @RequestMapping("/cidToPnam")
    public String cidToPnam(String cid){
        Contract contract = new Contract();
        contract.setId(cid);
        String pid = contractMapper.selectOne(contract).getProjectId();
        Project project = new Project();
        project.setId(pid);
        String pnam = projectMapper.selectOne(project).getProjectNam();
        return pnam;
    }

    //判断当前项目能不能新建合同
    @RequestMapping("/isXjht")
    public Boolean isXjht(String projectId){
        Contract contract=new Contract();
        contract.setProjectId(projectId);
        if(contractMapper.select(contract).size()==0)
            return true;
        return false;
    }
}
