package com.sq.demo.controller;


import com.sq.demo.mapper.AttachmentlinkMapper;
import com.sq.demo.mapper.TbdwMapper;
import com.sq.demo.mapper.ZhaobiaoMapper;
import com.sq.demo.pojo.Attachmentlink;
import com.sq.demo.pojo.Tbdw;
import com.sq.demo.pojo.Zhaobiao;
import com.sq.demo.utils.IdCreate;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/zhaobiao")
public class ZhaobiaoController {
    @Autowired
    AttachmentlinkMapper attachmentlinkMapper;
    @Autowired
    ZhaobiaoMapper zhaobiaoMapper;
    @Autowired
    TbdwMapper tbdwMapper;

    //作废招标
    @RequestMapping("/zfzb")
    public boolean zfzb(String id, String zbpid) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = engine.getRuntimeService();
            //删流程
            runtimeService.deleteProcessInstance(zbpid, "");
            //删表
            zhaobiaoMapper.deleteByPrimaryKey(id);
            //删投标商表
            Tbdw tbdw = new Tbdw();
            tbdw.setZbid(id);
            tbdwMapper.delete(tbdw);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //技术部经办人标书发起
    @RequestMapping("/bsfq")//招标id,发标时间，评标时间，投标截止时间,招标流程pid
    public boolean bsfq(String id, String fbsj, String pbsj, String tbjzsj, String userId) {
        try {
            Zhaobiao zhaobiao = zhaobiaoMapper.selectByPrimaryKey(id);
            zhaobiao.setPbsj(pbsj);
            zhaobiao.setFbsj(fbsj);
            zhaobiao.setTbjzsj(tbjzsj);
            zhaobiaoMapper.updateByPrimaryKeySelective(zhaobiao);
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult();
            //设置招标表参数
            taskService.setVariable(task.getId(), "zhaobiao", zhaobiao);
            //设置任务受理人
            taskService.setAssignee(task.getId(), userId);
            //设置参数
            taskService.setVariable(task.getId(), "jbr", true);
            //完成填写申请项目
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //删除投标商
    @RequestMapping("/deletTbdw")
    public boolean deletTbdw(String id) {
        return tbdwMapper.deleteByPrimaryKey(id) == 1;
    }

    //拿到所有投标商
    @RequestMapping("/getAllTbdw")
    public List<Tbdw> getAllTbdw(String zbid) {
        Tbdw tbdw = new Tbdw();
        tbdw.setZbid(zbid);
        return tbdwMapper.select(tbdw);
    }

    //添加投标商
    @RequestMapping("/addTbdw")
    public boolean addTbdw(String zbid, String dw) {
        Tbdw tbdw = new Tbdw();
        tbdw.setId(IdCreate.id());
        tbdw.setZbid(zbid);
        tbdw.setDw(dw);
        if (tbdwMapper.insert(tbdw) == 1)
            return true;
        return false;
    }

    //启动招标申请流程,办事员填写招标表
    @RequestMapping("/startZhaobiao")
    public String startZhaobiao(@RequestBody Zhaobiao zhaobiao) {
        zhaobiao.setId(IdCreate.id());
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        RuntimeService runtimeService = engine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("zbsp");
        zhaobiao.setZbpid(pi.getId());
        zhaobiaoMapper.insert(zhaobiao);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //设置招标表参数
        taskService.setVariable(task.getId(), "zhaobiao", zhaobiao);
        //设置任务受理人
        taskService.setAssignee(task.getId(), zhaobiao.getSqr());
        //完成填写申请项目
        taskService.complete(task.getId());
        System.out.println(zhaobiao.getId() + "_" + pi.getId());
        return zhaobiao.getId() + "_" + pi.getId();
    }

    //id查询所有项目 根据用户id拿到自己参与的项目
    @RequestMapping("/getAllzhaobiao")
    public List<Zhaobiao> getallproject(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> datas = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).list();
        List<Zhaobiao> zhaobiaos = new ArrayList<>();
        for (HistoricTaskInstance data : datas) {
            Zhaobiao zhaobiao = new Zhaobiao();
            zhaobiao.setZbpid(data.getProcessInstanceId());
            zhaobiao = zhaobiaoMapper.selectOne(zhaobiao);
            if (zhaobiao != null)
                zhaobiaos.add(zhaobiao);
        }
        List<Zhaobiao> res = new ArrayList<>();
        if (zhaobiaos.size() != 0)
            res.add(zhaobiaos.get(0));
        for (int i = 1; i < zhaobiaos.size(); i++) {
            boolean c = true;
            if (zhaobiaos.get(i) != null) {
                for (int j = 0; j < res.size(); j++) {
                    if (zhaobiaos.get(i).getZbpid().equals(res.get(j).getZbpid())) {
                        c = false;
                        break;
                    }
                }
                if (c)
                    res.add(zhaobiaos.get(i));
            }
        }
        return res;
    }

    //领取招标表单
    @RequestMapping("/lqzhaobiao")
    public List<Zhaobiao> lqzhaobiao(String userId) {
        System.out.println(userId);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        //查询职位
        Group group = identityService.createGroupQuery().groupMember(userId).singleResult();
        //查询部门
        String did = identityService.getUserInfo(userId, "departmentId");
        //查询组下面的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group.getId()).list();
        System.out.println(tasks.size());
        //过滤，查询自己部门下的任务,找到后返回
        List<Zhaobiao> zhaobiaos = new ArrayList<>();
        if (did.equals("20190123022801622") || did.equals("20190125102616787")) {//工程技术部或者办公室不用过滤
            for (Task task : tasks) {
                Zhaobiao zhaobiao = (Zhaobiao) taskService.getVariable(task.getId(), "zhaobiao");
                if (zhaobiao != null)
                    zhaobiaos.add(zhaobiao);
            }
        } else {//其他部门就需要过滤
            for (Task task : tasks) {
                Zhaobiao zhaobiao = (Zhaobiao) taskService.getVariable(task.getId(), "zhaobiao");
                if (zhaobiao != null && identityService.getUserInfo(zhaobiao.getSqr(), "departmentId").equals(did)) {//申请人部门一样
                    zhaobiaos.add(zhaobiao);
                }
            }
        }
        return zhaobiaos;
    }

    //上传附件
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String zbpid, String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            System.out.println(zbpid + ' ' + userId + ' ' + file.getOriginalFilename());
            //查找当前流程的任务
            Task task = taskService.createTaskQuery().processInstanceId(zbpid).singleResult();
            //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
            Attachment attachment = taskService.createAttachment("", task.getId(), zbpid, file.getOriginalFilename(), "", file.getInputStream());
            Attachmentlink attachmentlink = new Attachmentlink();
            attachmentlink.setUserid(userId);
            attachmentlink.setAttachment(attachment.getId());
            attachmentlinkMapper.insert(attachmentlink);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //处理节点(驳回处理)
    @RequestMapping("/doNode")
    public boolean doNode(String zbpid, String userId, String varName, String value) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zbpid).singleResult();
            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            //设置参数
            if (varName != "" && varName != null)
                taskService.setVariable(task.getId(), varName, value);
            //完成任务
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
