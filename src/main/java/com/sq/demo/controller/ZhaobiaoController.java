package com.sq.demo.controller;


import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.Time;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
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

import java.text.SimpleDateFormat;
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
    @Autowired
    ZhongbiaoMapper zhongbiaoMapper;
    @Autowired
    ProjectMapper projectMapper;

    //重新申请
    @Transactional
    @RequestMapping("/cxsq")
    public void cxsq(@RequestBody Zhaobiao zhaobiao) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult();
        //重新设置项目参数
        //taskService.setVariable(task.getId(), "zhaobiao", zhaobiao);
        //修改项目表
        zhaobiaoMapper.updateByPrimaryKeySelective(zhaobiao);
        taskService.complete(task.getId());
    }

    //作废招标
    @Transactional
    @RequestMapping("/zfzb")
    public boolean zfzb(String id, String zbpid) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = engine.getRuntimeService();
            //删流程
            runtimeService.deleteProcessInstance(zbpid, "");
            //删除历史
            HistoryService historyService=engine.getHistoryService();
            historyService.deleteHistoricProcessInstance(zbpid);
            //删招标表
            zhaobiaoMapper.deleteByPrimaryKey(id);
            //删投中标表
            Zhongbiao zhongbiao=new Zhongbiao();
            zhongbiao.setZbid(id);
            zhongbiaoMapper.delete(zhongbiao);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //技术部经办人标书发起
    @Transactional
    @RequestMapping("/bsfq")//招标id,发标时间，评标时间，投标截止时间,招标流程pid
    public boolean bsfq(String id,String userId,String comment) {
        try {
            Zhaobiao zhaobiao = zhaobiaoMapper.selectByPrimaryKey(id);
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult();
            //添加评论
            if(comment==null)
                comment="";
                Authentication.setAuthenticatedUserId(userId);
                taskService.addComment(task.getId(), zhaobiao.getZbpid(), comment);

            //设置招标表参数
            //taskService.setVariable(task.getId(), "zhaobiao", zhaobiao);
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
    @Transactional
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
    @Transactional
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
    @Transactional
    @RequestMapping("/startZhaobiao")
    public String startZhaobiao(@RequestBody Zhaobiao zhaobiao) {
        zhaobiao.setId(IdCreate.id());
        zhaobiao.setCjsj(Time.getNow());
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        RuntimeService runtimeService = engine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("zbsp");
        zhaobiao.setZbpid(pi.getId());
        zhaobiaoMapper.insert(zhaobiao);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //设置招标表参数
        //taskService.setVariable(task.getId(), "zhaobiao", zhaobiao);
        //设置任务受理人
        taskService.setAssignee(task.getId(), zhaobiao.getSqr());
        //完成填写申请项目
        taskService.complete(task.getId());

        IdentityService identityService=engine.getIdentityService();

        //根据userId查找职位
        String groupName = identityService.createGroupQuery().groupMember(zhaobiao.getSqr()).singleResult().getName();
        if(groupName.equals("技术部办事员")) {//技术部申请直接跳过办事员,
            task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
            taskService.setVariable(task.getId(), "jbr", true);
            taskService.setAssignee(task.getId(), zhaobiao.getSqr());
            taskService.complete(task.getId());
        }

        return zhaobiao.getId() + "_" + pi.getId();
    }

    //userid查询所有项目 根据用户id拿到自己参与的项目
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

    //主管经理或经理拿到自己部门所有的招标信息
    @RequestMapping("/getselfDptZb")
    public List<Zhaobiao> getselfDptZb(String userId){
        List<Zhaobiao> res=new ArrayList<>();
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        String dptId=identityService.getUserInfo(userId,"departmentId");
        List<Zhaobiao> zhaobiaos=zhaobiaoMapper.selectAll();
        for (Zhaobiao zhaobiao:zhaobiaos){
            String dpt=identityService.getUserInfo(zhaobiao.getSqr(),"departmentId");
            if(dpt.equals(dptId)){
                res.add(zhaobiao);
            }
        }
        return res;
    }

    //技术部经理拿所有
    @RequestMapping("/jsbjlGetAllZhaobiao")
    public List<Zhaobiao> jsbjlGetAllZhaobiao(){
        return zhaobiaoMapper.selectAll();
    }

    //pid拿招标
    Zhaobiao pidToZhaobiao(String pid){
        Zhaobiao zhaobiao=new Zhaobiao();
        zhaobiao.setZbpid(pid);
        return zhaobiaoMapper.selectOne(zhaobiao);
    }

    //领取招标表单
    @RequestMapping("/lqzhaobiao")
    public List<Zhaobiao> lqzhaobiao(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        //查询职位
        Group group = identityService.createGroupQuery().groupMember(userId).singleResult();
        //查询部门
        String did = identityService.getUserInfo(userId, "departmentId");
        //查询组下面的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group.getId()).list();
        //过滤，查询自己部门下的任务,找到后返回
        List<Zhaobiao> zhaobiaos = new ArrayList<>();
        if (did.equals("20190123022801622") || did.equals("20190125102616787")) {//工程技术部或者办公室不用过滤
            for (Task task : tasks) {
                Zhaobiao zhaobiao = pidToZhaobiao(task.getProcessInstanceId());//(Zhaobiao) taskService.getVariable(task.getId(), "zhaobiao");
                if (zhaobiao != null)
                    zhaobiaos.add(zhaobiao);
            }
        } else {//其他部门就需要过滤
            for (Task task : tasks) {
                Zhaobiao zhaobiao = pidToZhaobiao(task.getProcessInstanceId());//(Zhaobiao) taskService.getVariable(task.getId(), "zhaobiao");
                if (zhaobiao != null && identityService.getUserInfo(zhaobiao.getSqr(), "departmentId").equals(did)) {//申请人部门一样
                    zhaobiaos.add(zhaobiao);
                }
            }
        }
        //如果是办事员，拿到招标，判定是不是自己的申请人，
        if(group.getId().equals("doman")){
            List<Zhaobiao> res=new ArrayList<>();
            for(int i=0;i<zhaobiaos.size();i++){
                if(zhaobiaos.get(i).getSqr().equals(userId)){
                    res.add(zhaobiaos.get(i));
                }
            }
            return res;
        }
        //如果是技术部经办人、拿到招标、判断改项目的经办人是不是自己
        if(group.getId().equals("jsb_doman")){
            List<Zhaobiao> res=new ArrayList<>();
            for(Zhaobiao zhaobiao:zhaobiaos){
                Project project=projectMapper.selectByPrimaryKey(zhaobiao.getXmid());
                String userName=identityService.createUserQuery().userId(userId).singleResult().getFirstName();
                if(project.getBider()!=null&&!project.getBider().equals("")&&project.getBider().equals(userName)){
                    res.add(zhaobiao);
                }
            }
            return res;
        }
        //如果是技术部主管经理，拿到招标，判断该项目的技术部主管经理的审批是不是自己
        if(group.getId().equals("jsb_zgjl")){
            List<Zhaobiao> res=new ArrayList<>();
            for (Zhaobiao zhaobiao:zhaobiaos){
                Project project=projectMapper.selectByPrimaryKey(zhaobiao.getXmid());
                if(project.getPid()!=null&&!project.getPid().equals("")) {
                    ProjectController projectController = new ProjectController();
                    //列出所有审核意见,找到主管经理
                    List<Return_Comments> return_comments = projectController.projecttocomment(project.getPid());
                    for (int i = 0; i < return_comments.size(); i++) {
                        //评论人
                        User user = identityService.createUserQuery().userFirstName(return_comments.get(i).getUsernam()).singleResult();
                        //评论人的group
                        Group group1 = identityService.createGroupQuery().groupMember(user.getId()).singleResult();
                        //如果评论人的职位是技术部主管经理，同时评论人是自己
                        if (group1.getId().equals("jsb_zgjl") && user.getId().equals(userId)) {
                            res.add(zhaobiao);
                            break;
                        }
                    }
                }
            }
            return res;
        }
        //办公室
        if(group.getId().equals("bgs")){
            List<Zhaobiao> res=new ArrayList<>();
            for (Zhaobiao zhaobiao:zhaobiaos){
                Project project=projectMapper.selectByPrimaryKey(zhaobiao.getXmid());
                //列出所有审核意见,
                if(project.getPid()!=null&&!project.getPid().equals("")){
                    ProjectController projectController = new ProjectController();
                    List<Return_Comments> return_comments = projectController.projecttocomment(project.getPid());
                    for(int i=0;i<return_comments.size();i++){
                        //评论人
                        User user=identityService.createUserQuery().userFirstName(return_comments.get(i).getUsernam()).singleResult();
                        //评论人的group
                        Group group1=identityService.createGroupQuery().groupMember(user.getId()).singleResult();
                        //如果评论人的职位是技术部主管经理，同时评论人是自己
                        if(group1.getId().equals("bgs")&&user.getId().equals(userId)){
                            res.add(zhaobiao);
                            break;
                        }
                    }
                }
            }
            return res;
        }
        //技术部经理就一个人、不用过滤
            return zhaobiaos;
    }

    //上传附件
    @Transactional
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String zbpid, String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
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

    //完成招标
    @Transactional
    @RequestMapping("/wczb")
    public boolean wczb(String userId,String id,String fbsj,String dbsj,String tbjzsj,String comment){//tbjzsj是工期
        try {
            Zhaobiao zhaobiao=zhaobiaoMapper.selectByPrimaryKey(id);
            zhaobiao.setFbsj(fbsj);//发表时间
            zhaobiao.setDbsj(dbsj);//定标时间
            zhaobiao.setTbjzsj(tbjzsj);//工期
            //更新招标表
            zhaobiaoMapper.updateByPrimaryKeySelective(zhaobiao);
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult();
            //重新设置项目参数
            //taskService.setVariable(task.getId(), "zhaobiao", zhaobiao);
            if(comment==null)
                comment="";
                //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), zhaobiao.getZbpid(), comment);

            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            //完成任务
            taskService.complete(task.getId());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //处理节点(驳回处理)
    @Transactional
    @RequestMapping("/doNode")
    public boolean doNode(String zbpid, String userId, String varName, String value,String comment) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zbpid).singleResult();
            if(comment==null)
                comment="";
                //添加评论
                Authentication.setAuthenticatedUserId(userId);
                taskService.addComment(task.getId(), zbpid, comment);

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

    //pid拿评论
    @RequestMapping(value = "/getComment")
    public List<Return_Comments> projecttocomment(String zbpid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        List<Comment> comments = taskService.getProcessInstanceComments(zbpid);
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
            String unam = user.getFirstName();
            Return_Comments return_comments1 = new Return_Comments();
            return_comments1.setComment(comment.getFullMessage());
            return_comments1.setUsernam(unam);
            String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTime());
            return_comments1.setTime(dd);
            return_comments.add(return_comments1);
        }
        return return_comments;
    }

    //拿流程的当前节点
    @RequestMapping(value = "/getZhaobiaoNode")
    public String getZhaobiaoNode(String zbpid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(zbpid).singleResult();
        return task.getName();
    }
}
