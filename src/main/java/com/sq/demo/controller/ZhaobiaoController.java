package com.sq.demo.controller;


import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.GroupUtils;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.TaskUtil;
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
import java.util.Map;

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
    @Autowired
    ContractfileMapper contractfileMapper;
    @Autowired
    YscjdwjMapper yscjdwjMapper;
    @Autowired
    UserController userController;

    public boolean isJsb(String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        return identityService.getUserInfo(userId, "departmentId").equals("20190123022801622");
    }

    //判断招标是否可删除
    boolean canDeleZhaobiao(String id) {
        Zhaobiao zhaobiao = zhaobiaoMapper.selectByPrimaryKey(id);
        if (zhaobiao.getZbpid() == null || zhaobiao.getZbpid().equals(""))//没流程能删
            return true;
        else {
            if (isJsb(zhaobiao.getSqr())) {
                if (getZhaobiaoNode(zhaobiao.getZbpid()).equals("技术部经办人")) {
                    return true;
                }
                return false;
            } else {
                if (getZhaobiaoNode(zhaobiao.getZbpid()).equals("立项部门提出技术要求"))
                    return true;
                return false;
            }
        }
    }

    //删除招标
    @RequestMapping("/delete")
    boolean delete(String id) {
        if (id != null && !id.equals("")) {
            zhaobiaoMapper.deleteByPrimaryKey(id);
            return true;
        } else
            return false;
    }

    //判断该项目是否可以进行招标申请
    @RequestMapping("/canZhaobiaoSq")
    public boolean canZhaobiaoSq(String projectId) {
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (project.getPid() == null || project.getPid().equals(""))
            return false;
        String node = getZhaobiaoNode(project.getPid());
        if (node.equals("两会") || node.equals("申请结束") || node.equals("总经理办公会") || node.equals("备案"))
            return true;
        return false;
    }

    //重新申请
    @Transactional
    @RequestMapping("/cxsq")
    public boolean cxsq(String id, String jsyq, String comment, String userId) {
        try {
            Zhaobiao zhaobiao = zhaobiaoMapper.selectByPrimaryKey(id);
            zhaobiao.setId(id);
            zhaobiao.setJsyq(jsyq);
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult();
            //修改招标表
            zhaobiaoMapper.updateByPrimaryKeySelective(zhaobiao);
            //添加评论
            if (comment == null)
                comment = "";
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), zhaobiao.getZbpid(), comment);
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

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
            HistoryService historyService = engine.getHistoryService();
            historyService.deleteHistoricProcessInstance(zbpid);
            //删招标表
            zhaobiaoMapper.deleteByPrimaryKey(id);
            //删投中标表
            Zhongbiao zhongbiao = new Zhongbiao();
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
    public boolean bsfq(String id, String userId, String comment, String jsyq) {
        try {
            Zhaobiao zhaobiao = zhaobiaoMapper.selectByPrimaryKey(id);
            zhaobiao.setJsyq(jsyq);
            zhaobiaoMapper.updateByPrimaryKeySelective(zhaobiao);
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult();
            //添加评论
            if (comment == null)
                comment = "";
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), zhaobiao.getZbpid(), comment);
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

    //更新招标表
    @Transactional
    @RequestMapping("/updata")
    public boolean updata(String id, String xmid, String jsyq) {
        try {
            Zhaobiao zhaobiao = new Zhaobiao();
            zhaobiao.setId(id);
            zhaobiao.setXmid(xmid);
            zhaobiao.setJsyq(jsyq);
            zhaobiaoMapper.updateByPrimaryKeySelective(zhaobiao);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //办事员填写招标表,插入招标标
    @Transactional
    @RequestMapping("/insertZhaobiao")
    public String insertZhaobiao(@RequestBody Zhaobiao zhaobiao) {
        try {
            zhaobiao.setId(IdCreate.id());
            zhaobiao.setCjsj(Time.getNow());
            zhaobiaoMapper.insert(zhaobiao);
            return zhaobiao.getId() + ":" + zhaobiao.getCjsj();
        } catch (Exception e) {
            return "";
        }

    }

    //启动招标申请流程,
    @Transactional
    @RequestMapping("/startZhaobiao")
    public String startZhaobiao(String id) {
        // try {
        Zhaobiao zhaobiao = zhaobiaoMapper.selectByPrimaryKey(id);
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        RuntimeService runtimeService = engine.getRuntimeService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("zbsp");
        zhaobiao.setZbpid(pi.getId());
        zhaobiaoMapper.updateByPrimaryKeySelective(zhaobiao);
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //设置任务受理人
        taskService.setAssignee(task.getId(), zhaobiao.getSqr());
        //处理还没有申请时候的附件
        Contractfile contractfile = new Contractfile();
        contractfile.setCid(id);
        //拿到还没有申请时候的文件
        List<Contractfile> contractfiles = contractfileMapper.select(contractfile);
        System.out.println(contractfiles.size());
        RepositoryService repositoryService = engine.getRepositoryService();
        for (Contractfile contractfile1 : contractfiles) {
            //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
            Attachment attachment = taskService.createAttachment("", task.getId(), pi.getId(), contractfile1.getFname(), "", repositoryService.getResourceAsStream(contractfile1.getFid(), contractfile1.getFname()));
            Attachmentlink attachmentlink = new Attachmentlink();
            attachmentlink.setUserid(zhaobiao.getSqr());
            attachmentlink.setAttachment(attachment.getId());
            attachmentlinkMapper.insert(attachmentlink);
            //更新已上传节点文件的fid
            Yscjdwj yscjdwj = new Yscjdwj();
            //以前是部署id，未申请的时候用部署id下载
            yscjdwj.setFid(contractfile1.getFid());
            yscjdwj.setFname(contractfile1.getFname());
            yscjdwj.setJlid(id);
            yscjdwj = yscjdwjMapper.selectOne(yscjdwj);
            //申请了用附件id下载
            yscjdwj.setFid(attachment.getId());
            yscjdwjMapper.updateByPrimaryKeySelective(yscjdwj);
        }
        //完成填写申请项目
        taskService.complete(task.getId());
        //技术部经办人直接同意，因为现在直接由技术经办人发起
        task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        taskService.setVariable(task.getId(), "jbr", true);
        taskService.setAssignee(task.getId(), zhaobiao.getSqr());
        taskService.complete(task.getId());
        return pi.getId();
        // } catch (Exception e) {
        //     return "";
        //}
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
    public List<Zhaobiao> getselfDptZb(String departmentId) {
        List<Zhaobiao> res = new ArrayList<>();
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<Zhaobiao> zhaobiaos = zhaobiaoMapper.selectAll();
        for (Zhaobiao zhaobiao : zhaobiaos) {
            Project project = projectMapper.selectByPrimaryKey(zhaobiao.getXmid());
            //项目申请人
            User user = identityService.createUserQuery().userFirstName(project.getProposer()).singleResult();
            String dpt = identityService.getUserInfo(user.getId(), "departmentId");
            if (dpt.equals(departmentId)) {
                res.add(zhaobiao);
            }
        }
        //填充完整数据
        for (Zhaobiao zhaobiao : res) {
            addZhaobiaoData(zhaobiao);
        }
        return res;
    }

    //填充的各个数据（前端显示）
    public void addZhaobiaoData(Zhaobiao zhaobiao) {
        //节点填充
        if (zhaobiao.getZbpid() == null || zhaobiao.getZbpid().equals("")) {
            zhaobiao.setDqjd("未申请");
        } else {
            zhaobiao.setDqjd(getZhaobiaoNode(zhaobiao.getZbpid()));
        }
        //中标人、中标金额填充
        Zhongbiao zhongbiao = new Zhongbiao();
        zhongbiao.setZbid(zhaobiao.getId());
        zhongbiao = zhongbiaoMapper.selectOne(zhongbiao);
        if (zhongbiao != null) {
            zhaobiao.setZbr(zhongbiao.getZhongbiaodw());
            zhaobiao.setZbje(zhongbiao.getZhongbiaojg());
        }
        //项目名和项目编号的填充
        Map map = projectMapper.selectNameAndNo(zhaobiao.getXmid());
        if (map.get("project_no") != null)
            zhaobiao.setXmNo(map.get("project_no").toString());
        if (map.get("project_nam") != null)
            zhaobiao.setXmName(map.get("project_nam").toString());
        //填充申请人
        zhaobiao.setUserName(userController.userIdTouserName(zhaobiao.getSqr()));

    }

    //技术部经理拿所有
    @RequestMapping("/jsbjlGetAllZhaobiao")
    public List<Zhaobiao> jsbjlGetAllZhaobiao() {
        List<Zhaobiao> res = zhaobiaoMapper.selectAll();
        //填充完整数据
        for (Zhaobiao zhaobiao : res) {
            addZhaobiaoData(zhaobiao);
        }
        return res;
    }

    //pid拿招标
    Zhaobiao pidToZhaobiao(String pid) {
        Zhaobiao zhaobiao = new Zhaobiao();
        zhaobiao.setZbpid(pid);
        return zhaobiaoMapper.selectOne(zhaobiao);
    }

    //给招标填充部门
    public void fillBm(List<Zhaobiao> zhaobiaos) {
        for (Zhaobiao zhongbiao : zhaobiaos) {
            zhongbiao.setDeclarationDep(projectMapper.selectByPrimaryKey(zhongbiao.getXmid()).getDeclarationDep());
        }
    }

    //领取招标表单
    @RequestMapping("/lqzhaobiao")
    public List<Zhaobiao> lqzhaobiao(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        //查询职位
        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        //查询部门
        String did = identityService.getUserInfo(userId, "departmentId");
        //查询组下面的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groups.get(0).getId()).list();
        for (int i = 1; i < groups.size(); i++) {
            tasks.addAll(taskService.createTaskQuery().taskCandidateGroup(groups.get(0).getId()).list());
        }
        //过滤掉不是招标审批的
        TaskUtil.filterOtherLc(tasks, "zbsp");
        //过滤，查询自己部门下的任务,找到后返回
        List<Zhaobiao> zhaobiaos = new ArrayList<>();
        if (did.equals("20190123022801622") || did.equals("20190125102616787")) {//工程技术部或者办公室不用过滤
            for (Task task : tasks) {
                Zhaobiao zhaobiao = pidToZhaobiao(task.getProcessInstanceId());
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
        if (GroupUtils.equalsJs(groups, "doman")) {
            List<Zhaobiao> res = new ArrayList<>();
            for (int i = 0; i < zhaobiaos.size(); i++) {
                if (zhaobiaos.get(i).getSqr().equals(userId)) {
                    res.add(zhaobiaos.get(i));
                }
            }
            fillBm(res);
            return res;
        }
        //如果是技术部经办人、拿到招标、判断改项目的经办人是不是自己
        if (GroupUtils.equalsJs(groups, "jsb_doman")) {
            List<Zhaobiao> res = new ArrayList<>();
            for (Zhaobiao zhaobiao : zhaobiaos) {
                Project project = projectMapper.selectByPrimaryKey(zhaobiao.getXmid());
                String userName = identityService.createUserQuery().userId(userId).singleResult().getFirstName();
                if (project.getBider() != null && !project.getBider().equals("") && project.getBider().equals(userName)) {
                    res.add(zhaobiao);
                }
            }
            fillBm(res);
            return res;
        }
        //如果是技术部主管经理，拿到招标，判断该项目的技术部主管经理的审批是不是自己
        if (GroupUtils.equalsJs(groups, "jsb_zgjl")) {
            List<Zhaobiao> res = new ArrayList<>();
            for (Zhaobiao zhaobiao : zhaobiaos) {
                Project project = projectMapper.selectByPrimaryKey(zhaobiao.getXmid());
                if (project.getPid() != null && !project.getPid().equals("")) {
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
            fillBm(res);
            return res;
        }
        //办公室
        if (GroupUtils.equalsJs(groups, "bgs")) {
            List<Zhaobiao> res = new ArrayList<>();
            for (Zhaobiao zhaobiao : zhaobiaos) {
                Project project = projectMapper.selectByPrimaryKey(zhaobiao.getXmid());
                //列出所有审核意见,
                if (project.getPid() != null && !project.getPid().equals("")) {
                    ProjectController projectController = new ProjectController();
                    List<Return_Comments> return_comments = projectController.projecttocomment(project.getPid());
                    for (int i = 0; i < return_comments.size(); i++) {
                        //评论人
                        User user = identityService.createUserQuery().userFirstName(return_comments.get(i).getUsernam()).singleResult();
                        //评论人的group
                        Group group1 = identityService.createGroupQuery().groupMember(user.getId()).singleResult();
                        //如果评论人的职位是技术部主管经理，同时评论人是自己
                        if (group1.getId().equals("bgs") && user.getId().equals(userId)) {
                            res.add(zhaobiao);
                            break;
                        }
                    }
                }
            }
            fillBm(res);
            return res;
        }
        //技术部经理就一个人、不用过滤
        fillBm(zhaobiaos);
        return zhaobiaos;
    }

    //上传附件
    @Transactional
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String zbpid, String userId, String bcwjid) {
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
            Yscjdwj yscjdwj = new Yscjdwj();
            yscjdwj.setId(IdCreate.id());
            yscjdwj.setJlid(pidToZhaobiao(zbpid).getId());
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

    //完成招标
    @Transactional
    @RequestMapping("/wczb")
    public boolean wczb(String userId, String id, String fbsj, String dbsj, String tbjzsj, String comment) {//tbjzsj是工期
        try {
            Zhaobiao zhaobiao = zhaobiaoMapper.selectByPrimaryKey(id);
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
            if (comment == null)
                comment = "";
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), zhaobiao.getZbpid(), comment);

            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            //完成任务
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //处理节点(驳回处理)
    @Transactional
    @RequestMapping("/doNode")
    public boolean doNode(String zbpid, String userId, String varName, String value, String comment) {
        System.out.println(Time.getNow() + "  zbpid:" + zbpid);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(zbpid).singleResult();
            if (comment == null)
                comment = "";
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
            System.out.println(e);
            return false;
        }
    }

    //拿到招标的职位
    Group getZbjs(List<Group> groups) {
        List<String> qqjss = new ArrayList<>();
        qqjss.add("bgs");
        qqjss.add("doman");
        qqjss.add("jl");
        qqjss.add("jsb_doman");
        qqjss.add("jsb_jl");
        qqjss.add("jsb_zgjl");
        qqjss.add("zgjl");
        for (Group group : groups) {
            if (qqjss.contains(group.getId())) {
                return group;
            }
        }
        return null;
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
            List<Group> groups = identityService.createGroupQuery().groupMember(uid).list();
            Group group = getZbjs(groups);
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
    @RequestMapping(value = "/getZhaobiaoNode")
    public String getZhaobiaoNode(String zbpid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(zbpid).singleResult();
        return task.getName();
    }
}
