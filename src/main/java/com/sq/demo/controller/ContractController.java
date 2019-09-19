package com.sq.demo.controller;

import com.github.pagehelper.PageHelper;
import com.sq.demo.Entity.Contract_return;
import com.sq.demo.Entity.Hetong;
import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.GroupUtils;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.Time;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    YscjdwjMapper yscjdwjMapper;
    @Autowired
    AttachmentlinkMapper attachmentlinkMapper;
    @Autowired
    YanshouMapper yanshouMapper;
    @Autowired
    PayableMapper payableMapper;
    @Autowired
    ZhongbiaoMapper zhongbiaoMapper;
    @Autowired
    ZhaobiaoMapper zhaobiaoMapper;
    @Autowired
    XmsjbMapper xmsjbMapper;

    //综合搜索
    @RequestMapping("search")
    public List<Contract_return> search(String contractNo, String projectName, String ContractDate, String dfdsr, String tzwh, String dqjd) {
        List<Contract> contracts = contractMapper.search(contractNo, ContractDate, dfdsr, tzwh);
        List<Contract_return> res = new ArrayList<>();
        if ((projectName == null || projectName.equals("")) && (dqjd == null || dqjd.equals(""))) {//项目名称和节点没选择
            for (Contract contract : contracts) {
                res.add(contractTocontractreturn(contract));
            }
            return res;
        } else if ((projectName != null && !projectName.equals("")) && (dqjd == null || dqjd.equals(""))) {//项目名称搜索，节点没搜索
            for (Contract contract : contracts) {
                Project project = projectMapper.selectByPrimaryKey(contract.getProjectId());
                if (project.getProjectNam().contains(projectName)) {
                    res.add(contractTocontractreturn(contract));
                }
            }
            return res;
        } else if ((projectName == null || projectName.equals("")) && dqjd != null && !dqjd.equals("")) {//项目名称没搜索、节点有搜索
            if (dqjd.equals("未申请")) {
                for (Contract contract : contracts) {
                    if (contract.getDwyj() == null || contract.getDwyj().equals("")) {
                        res.add(contractTocontractreturn(contract));
                    }
                }
                return res;
            } else {
                for (Contract contract : contracts) {
                    if (contract.getDwyj() != null && !contract.getDwyj().equals("") && getPidNode(contract.getDwyj()).equals(dqjd)) {
                        res.add(contractTocontractreturn(contract));
                    }
                }
                return res;
            }
        } else {//项目名称和节点都有搜索
            for (Contract contract : contracts) {
                Project project = projectMapper.selectByPrimaryKey(contract.getProjectId());
                if (project.getProjectNam().contains(projectName) && contract.getDwyj() != null && !contract.getDwyj().equals("") && getPidNode(contract.getDwyj()).equals(dqjd)) {
                    res.add(contractTocontractreturn(contract));
                }
            }
            return res;
        }
    }

    //确认合同已接受
    @RequestMapping("qrhtyjs")
    public boolean qrhtyjs(String dwyj, String htno) {
        try {
            Contract contract = new Contract();
            contract.setDwyj(dwyj);
            contract = contractMapper.selectOne(contract);
            contract.setContractNo(htno);
            contractMapper.updateByPrimaryKeySelective(contract);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(dwyj).singleResult();
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    //合同再次审批
    @RequestMapping("htzcsp")
    public boolean htzcsp(@RequestBody Contract contract) {
        try {
            contractMapper.updateByPrimaryKeySelective(contract);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(contract.getDwyj()).singleResult();
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //合同作废
    @RequestMapping("htzf")
    public boolean htzf(String htid) {
        try {
            Contract contract = contractMapper.selectByPrimaryKey(htid);
            //删表
            contractMapper.delete(contract);
            //删流程
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = engine.getRuntimeService();
            runtimeService.deleteProcessInstance(contract.getDwyj(), "");
            //删除历史
            HistoryService historyService = engine.getHistoryService();
            historyService.deleteHistoricProcessInstance(contract.getDwyj());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //开始合同审批
    @RequestMapping("/startHtsp")
    @Transactional
    public boolean startHtsp(String htid) {
        try {
            Contract contract = contractMapper.selectByPrimaryKey(htid);
            if (contract.getDwyj() == null || contract.getDwyj().equals("")) {
                ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
                TaskService taskService = engine.getTaskService();
                RuntimeService runtimeService = engine.getRuntimeService();
                ProcessInstance pi = runtimeService.startProcessInstanceByKey("htsp");
                contract.setDwyj(pi.getId());
                //填写流程id
                contractMapper.updateByPrimaryKeySelective(contract);
                Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                //处理前期还未申请时候的附件
                Contractfile contractfile = new Contractfile();
                contractfile.setCid(htid);
                List<Contractfile> contractfiles = contractfileMapper.select(contractfile);
                RepositoryService repositoryService = engine.getRepositoryService();
                for (Contractfile contractfile1 : contractfiles) {
                    //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
                    Attachment attachment = taskService.createAttachment("", task.getId(), pi.getId(), contractfile1.getFname(), "", repositoryService.getResourceAsStream(contractfile1.getFid(), contractfile1.getFname()));
                    //更新已上传节点文件的fid
                    Yscjdwj yscjdwj = new Yscjdwj();
                    //以前是部署id，为申请的时候用部署id下载
                    yscjdwj.setFid(contractfile1.getFid());
                    yscjdwj.setFname(contractfile1.getFname());
                    yscjdwj.setJlid(htid);
                    yscjdwj = yscjdwjMapper.selectOne(yscjdwj);
                    //申请了用附件id下载
                    yscjdwj.setFid(attachment.getId());
                    yscjdwjMapper.updateByPrimaryKeySelective(yscjdwj);
                }
                taskService.complete(task.getId());
                //合同时间记录
                Xmsjb xmsjb = new Xmsjb();
                xmsjb.setProjectid(contract.getProjectId());
                xmsjb.setHtkssj(Time.getNow());
                xmsjbMapper.updateByPrimaryKeySelective(xmsjb);
                return true;
            } else {
                return htzcsp(contract);
            }
        } catch (Exception e) {
            return false;
        }

    }

    //合同处理
    @RequestMapping("doht")
    public boolean doht(String dwyj, String userId, String value, String comment) {
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            IdentityService identityService = processEngine.getIdentityService();
            Task task = taskService.createTaskQuery().processInstanceId(dwyj).singleResult();
            Authentication.setAuthenticatedUserId(userId);
            if (comment == null)
                comment = "";
            taskService.addComment(task.getId(), dwyj, comment);
            List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
            String node = getPidNode(dwyj);
            if (GroupUtils.equalsJs(groups, "jsb_doman") && node.equals("经办人")) {
                //经办人false作废合同流程
                if (value.equals("false")) {
                    Contract contract = new Contract();
                    contract.setDwyj(dwyj);
                    contract = contractMapper.selectOne(contract);
                    //流程id去掉
                    contract.setDwyj(null);
                    contractMapper.updateByPrimaryKeySelective(contract);
                    ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
                    RuntimeService runtimeService = engine.getRuntimeService();
                    //删流程
                    runtimeService.deleteProcessInstance(dwyj, "");
                    //删除历史
                    HistoryService historyService = engine.getHistoryService();
                    historyService.deleteHistoricProcessInstance(dwyj);
                } else {//经办人再次审批
                    taskService.complete(task.getId());
                }
                return true;
            } else if (GroupUtils.equalsJs(groups, "jsb_jl")) {
                taskService.setVariable(task.getId(), "jsb_jl", value);
                //完成任务
                taskService.complete(task.getId());
                return true;
            } else if (GroupUtils.equalsJs(groups, "psr") && node.equals("评审人")) {
                taskService.setVariable(task.getId(), "psr", value);
                //完成任务
                taskService.complete(task.getId());
                return true;
            } else if (GroupUtils.equalsJs(groups, "fl")) {
                taskService.setVariable(task.getId(), "fl", value);
                //完成任务
                taskService.complete(task.getId());
                return true;
            } else if (GroupUtils.equalsJs(groups, "cw")) {
                taskService.setVariable(task.getId(), "cw", value);
                //完成任务
                taskService.complete(task.getId());
                return true;
            } else if (GroupUtils.equalsJs(groups, "fgfz")) {
                taskService.setVariable(task.getId(), "fgfz", value);
                //完成任务
                taskService.complete(task.getId());
                return true;
            } else if (GroupUtils.equalsJs(groups, "zjl")) {
                taskService.setVariable(task.getId(), "zjl", value);
                //完成任务
                taskService.complete(task.getId());
                return true;
            } else {//签订和归档
                taskService.complete(task.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //领取签订合同
    @RequestMapping("getQdht")
    public List<Contract_return> getQdht(String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        List<Contract> contracts = contractMapper.selectYlc();
        List<Contract_return> res = new ArrayList<>();
        if (GroupUtils.equalsJs(groups, "jsb_doman")) {
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("签订") && contract.getCwbmyj().equals(userId)) {
                    res.add(contractTocontractreturn(contract));
                }
            }
        } else {//评审人
            User user = identityService.createUserQuery().userId(userId).singleResult();
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("签订")) {
                    List<Return_Comments> return_comments = getHtComment(contract.getDwyj());
                    for (Return_Comments return_comments1 : return_comments) {
                        if (return_comments1.getUsernam().equals(user.getFirstName())) {
                            res.add(contractTocontractreturn(contract));
                            break;
                        }
                    }
                }
            }
        }
        return res;
    }

    //领取合同
    @RequestMapping("lqht")
    public List<Contract_return> lqht(String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        List<Contract> contracts = contractMapper.selectYlc();
        List<Contract_return> res = new ArrayList<>();
        if (GroupUtils.equalsJs(groups, "jsb_doman")) {
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                //节点经办人、且是自己发起的
                if (node.equals("经办人") && contract.getCwbmyj().equals(userId)) {
                    res.add(contractTocontractreturn(contract));
                }
            }
        } else if (GroupUtils.equalsJs(groups, "jsb_jl")) {
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("技术部经理")) {
                    res.add(contractTocontractreturn(contract));
                }
            }
        } else if (GroupUtils.equalsJs(groups, "psr")) {
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("评审人")) {
                    res.add(contractTocontractreturn(contract));
                }
            }
        } else if (GroupUtils.equalsJs(groups, "fl")) {
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("法律事务室")) {
                    res.add(contractTocontractreturn(contract));
                }
            }
        } else if (GroupUtils.equalsJs(groups, "cw")) {
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("财务总监")) {
                    res.add(contractTocontractreturn(contract));
                }
            }
        } else if (GroupUtils.equalsJs(groups, "fgfz")) {
            //分管副总管理的类型
            List<String> manageTypes= Arrays.asList(identityService.getUserInfo(userId,"manageType").split(","));
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("分管副总经理")) {
                    Project project = projectMapper.selectByPrimaryKey(contract.getProjectId());
                    if ( manageTypes.contains(project.getReviser())) {
                        res.add(contractTocontractreturn(contract));
                    }
                }
            }
        } else if (GroupUtils.equalsJs(groups, "zjl")) {
            for (Contract contract : contracts) {
                String node = getPidNode(contract.getDwyj());
                if (node.equals("总经理")) {
                    res.add(contractTocontractreturn(contract));
                }
            }
        }
        return res;
    }

    //拿到招标的职位
    Group getHtjs(List<Group> groups) {
        List<String> qqjss = new ArrayList<>();
        qqjss.add("jsb_doman");
        qqjss.add("jsb_jl");
        qqjss.add("psr");
        qqjss.add("fl");
        qqjss.add("cw");
        qqjss.add("fgfz");
        qqjss.add("zjl");
        for (Group group : groups) {
            if (qqjss.contains(group.getId())) {
                return group;
            }
        }
        return null;
    }

    //pid拿评论
    @RequestMapping(value = "/getComment")
    public List<Return_Comments> getHtComment(String dwyj) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        List<Comment> comments = taskService.getProcessInstanceComments(dwyj);
        List<Return_Comments> return_comments = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getType().equals("event")) {
                comments.remove(i);
                i--;
            }
        }
        for (Comment comment : comments) {
            String uid = comment.getUserId();
            User user = identityService.createUserQuery().userId(uid).singleResult();
            List<Group> groups = identityService.createGroupQuery().groupMember(uid).list();
            Group group = getHtjs(groups);
            String unam = user.getFirstName();
            Return_Comments return_comments1 = new Return_Comments();
            return_comments1.setComment(comment.getFullMessage());
            return_comments1.setUsernam(unam);
            return_comments1.setGroupName(group.getName());
            String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTime());
            return_comments1.setTime(dd);
            return_comments.add(return_comments1);
        }
        return return_comments;
    }

    //拿流程的当前节点
    @RequestMapping(value = "/getHtNode")
    public String getPidNode(String dwyj) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(dwyj).singleResult();
        return task.getName();
    }

    //归档
    @RequestMapping("/guidang")
    @Transactional
    public boolean guidang(String id, String dwyj) {
        try {
            Contract contract = contractMapper.selectByPrimaryKey(id);
            contract.setGd("1");
            contractMapper.updateByPrimaryKeySelective(contract);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(dwyj).singleResult();
            //填充合同结束时间
            Xmsjb xmsjb = new Xmsjb();
            xmsjb.setProjectid(contract.getProjectId());
            xmsjb.setHtjssj(Time.getNow());
            xmsjbMapper.updateByPrimaryKeySelective(xmsjb);
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //修改合同
    @RequestMapping("/updateContract")
    @Transactional
    public boolean updateContract(@RequestBody Contract con) {
        if (contractMapper.updateByPrimaryKey(con) == 1)
            return true;
        else
            return false;
    }

    //增加合同
    @RequestMapping("/addContract")
    @Transactional
    public String addContract(@RequestBody Contract con) {
        con.setId(IdCreate.id());
        con.setCjsj(Time.getNow());
        contractMapper.insert(con);
        return con.getId();
    }

    //删除合同
    @RequestMapping("/deleteContract")
    @Transactional
    public boolean deleteContract(String cid) {
        if (cid != null && !cid.equals("")) {
            Contractfile contractfile = new Contractfile();
            contractfile.setCid(cid);
            //删文件关联表
            contractfileMapper.delete(contractfile);
            if (contractMapper.deleteByPrimaryKey(cid) == 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    //合同搜索
    @RequestMapping("/contractNoss")
    public List<Contract_return> contractNoss(String contractNo) {
        List<Contract> contracts = contractMapper.contractNoss(contractNo);
        List<Contract_return> contract_returns = new ArrayList<>();
        for (Contract contract : contracts) {
            contract_returns.add(contractTocontractreturn(contract));
        }
        return contract_returns;
    }

    //合同id搜索
    @RequestMapping("/xmIdSS")
    public Contract_return xmIdSS(String xmId) {
        Contract contract = new Contract();
        contract.setProjectId(xmId);
        contract = contractMapper.selectOne(contract);
        if (contract == null)
            return null;
        return contractTocontractreturn(contract);
    }

    //查询总数
    @RequestMapping("/AllCounts")
    public int AllCounts() {
        return contractMapper.AllCounts();
    }

    //查询所有合同
    @RequestMapping("/getAllContracts")
    public List<Contract_return> getAllContracts(int pageNum) {
        PageHelper.startPage(pageNum, 10);
        List<Contract> contracts = contractMapper.selectAll();
        List<Contract_return> contract_returns = new ArrayList<>();
        for (Contract contract : contracts) {
            contract_returns.add(contractTocontractreturn(contract));
        }
        return contract_returns;
    }

    //contract转变为Contract_return
    public Contract_return contractTocontractreturn(Contract contract) {
        Project project = new Project();
        project.setId(contract.getProjectId());
        Project p1 = projectMapper.selectOne(project);
        String projectname = p1.getProjectNam();
        Contract_return c = new Contract_return();
        c.setXmNo(p1.getProjectNo());
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
        c.setKsyxq(contract.getKsyxq());
        c.setJsyxq(contract.getJsyxq());
        c.setGq(contract.getGq());
        c.setLxqk(contract.getLxqk());
        c.setDeclarationDep(p1.getDeclarationDep());
        c.setCjsj(contract.getCjsj());
        //填充当前节点
        if (contract.getDwyj() == null || contract.getDwyj().equals(""))
            c.setDqjd("未申请");
        else
            c.setDqjd(getPidNode(contract.getDwyj()));
        return c;
    }

    //拿到日期
    @RequestMapping("/getRq")
    public String getRq(String contractId) {
        Contract contract = new Contract();
        contract.setId(contractId);
        return contractMapper.selectOne(contract).getRq();
    }

    //拿到附件
    @RequestMapping("/getFjs")
    public List<Contractfile> getFjs(String cid) {
        Contractfile contractfile = new Contractfile();
        contractfile.setCid(cid);
        return contractfileMapper.select(contractfile);
    }

    //上传附件，已经申请
    @Transactional
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String dwyj, String userId, String bcwjid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            //查找当前流程的任务
            Task task = taskService.createTaskQuery().processInstanceId(dwyj).singleResult();
            //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
            Attachment attachment = taskService.createAttachment("", task.getId(), dwyj, file.getOriginalFilename(), "", file.getInputStream());
            Attachmentlink attachmentlink = new Attachmentlink();
            attachmentlink.setUserid(userId);
            attachmentlink.setAttachment(attachment.getId());
            attachmentlinkMapper.insert(attachmentlink);
            Yscjdwj yscjdwj = new Yscjdwj();
            yscjdwj.setId(IdCreate.id());
            yscjdwj.setJlid(DwyjToContract(dwyj).getId());
            yscjdwj.setBcwjid(bcwjid);
            yscjdwj.setFid(attachment.getId());
            yscjdwj.setFname(file.getOriginalFilename());
            yscjdwj.setScr(userId);
            yscjdwj.setY1(Time.getNow());
            yscjdwjMapper.insert(yscjdwj);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    Contract DwyjToContract(String dwyj) {
        Contract contract = new Contract();
        contract.setDwyj(dwyj);
        return contractMapper.selectOne(contract);
    }

    //上传附件,未申请
    @RequestMapping("/uploadHtfj")
    @Transactional
    public boolean uploadHtfj(MultipartFile file, String id, String bcwjid, String userId) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            RepositoryService repositoryService = engine.getRepositoryService();
            InputStream isq = file.getInputStream();
            DeploymentBuilder builder = repositoryService.createDeployment();
            builder.addInputStream(file.getOriginalFilename(), isq);
            Contractfile contractfile = new Contractfile();
            contractfile.setId(IdCreate.id());
            contractfile.setCid(id);
            contractfile.setFname(file.getOriginalFilename());
            String fid = builder.deploy().getId();
            contractfile.setFid(fid);
            contractfile.setScr(userId);
            contractfile.setScsj(Time.getNow());
            contractfileMapper.insert(contractfile);
            if (bcwjid != null && !bcwjid.equals("")) {
                Yscjdwj yscjdwj = new Yscjdwj();
                yscjdwj.setId(IdCreate.id());
                yscjdwj.setJlid(id);
                yscjdwj.setBcwjid(bcwjid);
                //fid是部署id,并不是附件id
                yscjdwj.setFid(fid);
                yscjdwj.setFname(file.getOriginalFilename());
                yscjdwj.setScr(userId);
                yscjdwj.setY1(Time.getNow());
                yscjdwjMapper.insert(yscjdwj);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    //删除附件
    @RequestMapping("/deletFj")
    public boolean deletFj(String fid, String fname, String jlid) {
        if (fid != null) {
            Contractfile contractfile = new Contractfile();
            contractfile.setFid(fid);
            contractfileMapper.delete(contractfile);
            //必传节点文件去找,找到删除
            Yscjdwj yscjdwj = new Yscjdwj();
            yscjdwj.setFid(fid);
            yscjdwjMapper.delete(yscjdwj);
            return true;
        }
        return false;
    }

    //拿所有可以新建结算的合同id和合同号
    @RequestMapping("/canHtidAndHtno")
    public List<Hetong> canHtidAndHtno(String userId) {
        //拿到所有付完了的合同id
        List<String> htids = payableMapper.getFwHtid();
        List<Hetong> hetongs = getallhtid();
        for (String htid : htids) {
            for (int i = 0; i < hetongs.size(); i++) {
                if (hetongs.get(i).value.equals(htid)) {
                    hetongs.remove(i);
                    break;
                }
            }
        }
        //同时还要验收
        for (int i = 0; i < hetongs.size(); i++) {
            //合同对应的项目id
            String xmid = contractMapper.selectByPrimaryKey(hetongs.get(i).value).getProjectId();
            //检查改项目id有没有验收
            Yanshou yanshou = new Yanshou();
            yanshou.setProjectid(xmid);
            if (yanshouMapper.selectOne(yanshou) == null) {
                hetongs.remove(i);
                i--;
            }
        }
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        //过滤，不是自己的项目
        for (int i = 0; i < hetongs.size(); i++) {
            String xmid = contractMapper.selectByPrimaryKey(hetongs.get(i).value).getProjectId();
            String jbr = projectMapper.selectByPrimaryKey(xmid).getBider();
            String userName = identityService.createUserQuery().userId(userId).singleResult().getFirstName();
            if (!jbr.equals(userName)) {
                hetongs.remove(i);
                i--;
            }
        }
        return hetongs;
//        //所有验收了的项目id;
//        List<String> ysxmids=yanshouMapper.getXmids();
//        //所有验收了的合同id
//        List<String> htids=new ArrayList<>();
//        for(String ysxmid:ysxmids){
//            Contract contract=new Contract();
//            contract.setProjectId(ysxmid);
//            contract=contractMapper.selectOne(contract);
//            htids.add(contract.getId());
//        }
//        //过滤、付完了的合同id
//        for(String xxx:htid){
//            htids.remove(xxx);
//        }
//        List<Hetong> res=new ArrayList<>();
//        for(String yyy:htids){
//            Hetong hetong=new Hetong();
//            hetong.value=yyy;
//            hetong.label=contractMapper.selectByPrimaryKey(yyy).getContractNo();
//            res.add(hetong);
//        }
//        return res;
    }

    //拿到所有合同id和合同编号
    @RequestMapping("/getAllHtidAndHtno")
    public List<Hetong> getallhtid() {
        List<Contract> contracts = contractMapper.selectAll();
        List<Hetong> hetongs = new ArrayList<>();
        for (Contract contract : contracts) {
            if (contract.getContractNo() != null && !contract.getContractNo().equals("")) {
                Hetong hetong = new Hetong();
                hetong.value = contract.getId();
                hetong.label = contract.getContractNo();
                hetongs.add(hetong);
            }
        }


        return hetongs;
    }

    //查询合同
    @RequestMapping("/selectContract")
    public Contract selectContract(String contractId) {
        return contractMapper.selectByPrimaryKey(contractId);
    }

    //根据合同id拿到对方当事人
    @RequestMapping("/getDfdsr")
    public String getDfdsr(String contractId) {
        Contract contract = new Contract();
        contract.setId(contractId);
        String name = contractMapper.selectOne(contract).getDfdsr();
        return name;
    }

    //根据合同id拿到项目名称
    @RequestMapping("/cidToPnam")
    public String cidToPnam(String cid) {
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
    public Boolean isXjht(String projectId) {
        Contract contract = new Contract();
        contract.setProjectId(projectId);
        if (contractMapper.select(contract).size() == 0)
            return true;
        return false;
    }

    //新建合同，填充其他字段，参数projectId
    @RequestMapping("/fillContractByXmid")
    public String fillContractByXmid(String projectId) {
        Zhongbiao zhongbiao = new Zhongbiao();
        zhongbiao.setXmid(projectId);
        zhongbiao = zhongbiaoMapper.selectOne(zhongbiao);
        Zhaobiao zhaobiao = new Zhaobiao();
        zhaobiao.setXmid(projectId);
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (zhongbiao != null) {
            return zhongbiao.getZhongbiaodw() + "/" + zhongbiao.getZhongbiaojg() + "/" + project.getProjectNo() + "/" + project.getBider();
        } else {
            return project.getProjectNo() + "/" + project.getBider();
        }
    }

    //项目id拿工期
    @RequestMapping("/getGq")
    public String getGq(String projectId) {
        Zhaobiao zhaobiao = new Zhaobiao();
        zhaobiao.setXmid(projectId);
        zhaobiao = zhaobiaoMapper.selectOne(zhaobiao);
        if (zhaobiao != null)
            return zhaobiao.getTbjzsj();
        else
            return "";
    }

    //填写合同日期(签订日期)
    @RequestMapping("/savaQdrqAndContractNo")
    public boolean savaQdrqAndContractNo(String id, String rq, String no) {
        try {
            Contract contract = new Contract();
            contract.setId(id);
            contract.setRq(rq);
            contract.setContractNo(no);
            contractMapper.updateByPrimaryKeySelective(contract);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
