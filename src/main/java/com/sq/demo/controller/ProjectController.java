package com.sq.demo.controller;

import com.sq.demo.Entity.Project_Receive;
import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.Entity.Xm;
import com.sq.demo.mapper.AttachmentlinkMapper;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Attachmentlink;
import com.sq.demo.pojo.Department;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.IdCreate;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;


/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/projectApplication")
public class ProjectController {
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    AttachmentlinkMapper  attachmentlinkMapper;

    //判断当前项目是否可以申请
    @RequestMapping("/isSq")
    public boolean isSq(String projectId){
        Project project=projectMapper.selectByPrimaryKey(projectId);
        if(project.getPid()==null||project.getPid().equals(""))
            return true;
        if(getPidNode(project.getPid()).equals("填写申请表"))
            return true;
        return false;
    }

    //项目名称模糊搜索
    @RequestMapping("/xmmcss")
    public List<Project> xmmcss(String projectName){
        return projectMapper.xmmcss(projectName);
    }

    //项目编号模糊搜索
    @RequestMapping("/xmbhss")
    public List<Project> xmbhss(String projectNo){
        return projectMapper.xmbhss(projectNo);
    }

    //拿所有的项目
    @RequestMapping("/getAllProject")
    public List<Project> getAllProject(){
        return projectMapper.selectAll();
    }

    //从前期管理开始申请
    @RequestMapping("/qqglStartSq")
    public String qqglStartSq(@RequestBody Project project){
        if(project.getPid()==null||project.getPid().equals("")) {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            IdentityService identityService = engine.getIdentityService();
            //申请人姓名
            String unam = project.getProposer();
            TaskService taskService = engine.getTaskService();
            RuntimeService runtimeService = engine.getRuntimeService();
            //根据姓名查找userId
            User user = identityService.createUserQuery().userFirstName(project.getProposer()).singleResult();
            //根据userId查找职位
            String groupName = identityService.createGroupQuery().groupMember(user.getId()).singleResult().getName();
            ProcessInstance pi;
            System.out.println(groupName);
            if (groupName.equals("办事员")) {//其他部门项目
                pi = runtimeService.startProcessInstanceByKey("lxsp");
            } else {//技术部项目
                pi = runtimeService.startProcessInstanceByKey("jsb_lxsp");
            }
            Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
            project.setPid(pi.getId());
            projectMapper.updateByPrimaryKeySelective(project);
            //设置项目参数
            taskService.setVariable(task.getId(), "project", project);
            //设置任务受理人
            taskService.setAssignee(task.getId(), user.getId());
            //完成填写申请项目
            taskService.complete(task.getId());
            return pi.getId();
        }
        else {
            cxsq(project);
            return "";
        }
    }

    //启动申请流程,办事员填写申请表
    @RequestMapping("/startApplication")
    public String startApplication(@RequestBody Project_Receive pa){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //申请人姓名
        String unam = identityService.createUserQuery().userId(pa.getUserId()).singleResult().getFirstName();
        Project project = new Project();
        //部门名字
        String dnam = dnam(identityService.getUserInfo(pa.getUserId(),"departmentId"));
        String project_id = IdCreate.id();
        project.setId(project_id);
        project.setProjectNam(pa.getProject_name());
        project.setProjectType(pa.getProject_type());
        project.setPersonInCharge(pa.getPerson_in_charge());
        project.setProposer(unam);
        project.setApplicationDte(getnowtime());
        project.setInvestmentEstimate(pa.getInvestment_establish());
        project.setEstablishReason(pa.getEstablish_reason());
        project.setScale(pa.getScale());
        project.setIllustration(pa.getIllustration());
        project.setDeclarationDep(dnam);
        TaskService taskService=processEngine.getTaskService();
        RuntimeService runtimeService=processEngine.getRuntimeService();
        //根据userId查找职位
        String groupName=identityService.createGroupQuery().groupMember(pa.getUserId()).singleResult().getName();
        String res;
        ProcessInstance pi;
        if(groupName.equals("办事员")){//其他部门项目
            pi=runtimeService.startProcessInstanceByKey("lxsp");
        }else {//技术部项目
            pi=runtimeService.startProcessInstanceByKey("jsb_lxsp");
        }
        Task task=taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        project.setPid(pi.getId());
        projectMapper.insert(project);
        //设置项目参数
        taskService.setVariable(task.getId(),"project",project);
        //设置任务受理人
        taskService.setAssignee(task.getId(),pa.getUserId());
        //完成填写申请项目
        taskService.complete(task.getId());
        res = project_id+'_'+pi.getId();
        return res;
    }

    //重新申请
    @RequestMapping("/cxsq")
    public void cxsq(@RequestBody Project project){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        TaskService taskService=engine.getTaskService();
        Task task=taskService.createTaskQuery().processInstanceId(project.getPid()).singleResult();
        //重新设置项目参数
        taskService.setVariable(task.getId(),"project",project);
        //修改项目表
        projectMapper.updateByPrimaryKeySelective(project);
        taskService.complete(task.getId());
    }

    //处理节点
    @RequestMapping("/doNode")
    public boolean doNode(String pid,String userId,String comment,String varName,String value){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            System.out.println(pid+'_'+userId+'_'+comment+'_'+varName+'_'+value);
            TaskService taskService=processEngine.getTaskService();
            Task task=taskService.createTaskQuery().processInstanceId(pid).singleResult();
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(),pid,comment);
            //设置任务代理人
            taskService.setAssignee(task.getId(),userId);
            //设置参数
            taskService.setVariable(task.getId(),varName,value);
            //完成任务
            taskService.complete(task.getId());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //完成备案
    @RequestMapping("/wcba")
    public void wcba(String pid,String userId){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService=processEngine.getTaskService();
        Task task=taskService.createTaskQuery().processInstanceId(pid).singleResult();
        //设置任务代理人
        taskService.setAssignee(task.getId(),userId);
        //完成任务
        taskService.complete(task.getId());
    }

    //判断当前流程的经办人
    public boolean isJbs(String pid,String userId){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService=engine.getHistoryService();
        List<HistoricTaskInstance> datas=historyService.createHistoricTaskInstanceQuery().processInstanceId(pid).list();
        for(HistoricTaskInstance data:datas){
            if(data.getName().equals("经办人")&&data.getAssignee().equals(userId)){
                return true;
            }
        }
        return false;
    }

    //当前时间
    public String getnowtime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    //id查询所有项目 根据用户id拿到自己参与的项目
    @RequestMapping("/getallproject")
    public List<Project> getallproject(String userId){
        System.out.println(userId);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> datas = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).list();
        List<Project> p = new ArrayList<>();
        for(HistoricTaskInstance data : datas){
            Project po = new Project();
            po.setPid(data.getProcessInstanceId());
            p.add(projectMapper.selectOne(po));
        }
        System.out.println(p);
        List<Project> res=new ArrayList<>();
        if(p.size()!=0)
            res.add(p.get(0));
        for (int i=1;i<p.size();i++){
            boolean c=true;
            for(int j=0;j<res.size();j++){
                if(p.get(i).getPid().equals(res.get(j).getPid())){
                    c=false;
                    break;
                }
            }
            if(c)
                res.add(p.get(i));
        }
        return res;
    }

    //部门id转name
    public String dnam(String departmentId){
        Department department = new Department();
        department.setId(departmentId);
        String name = departmentMapper.selectOne(department).getdNam();
        return name;
    }

    //部门id查项目
    @RequestMapping("/didtoproject")
    public List<Project> didtoproject(String departmentId){
        String dnam = dnam(departmentId);
        Project project = new Project();
        project.setDeclarationDep(dnam);
        List<Project> po = projectMapper.select(project);
        return po;
    }

    //领取消息数
    @RequestMapping("/lqxxs")
    public int lqxxs(String userId){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService=processEngine.getTaskService();
        //查询职位
        Group group=identityService.createGroupQuery().groupMember(userId).singleResult();
        //查询部门
        String did=identityService.getUserInfo(userId,"departmentId");
        //查询组下面的任务
        List<Task> tasks=taskService.createTaskQuery().taskCandidateGroup(group.getId()).list();
        if(tasks==null)
            return 0;
        //过滤，查询自己部门下的任务
        int i=0;
        if(did.equals("20190123022801622")||did.equals("20190125102616787")){//工程技术部，全部返回
            return tasks.size();
        }else{
            for(Task task:tasks){
                Project project =(Project) taskService.getVariable(task.getId(),"project");
                if(project!=null&&project.getDeclarationDep().equals(dnam(did))){
                    i++;
                }
            }
        }
        return i;
    }

    //判断改项目有没有达到备案
    @RequestMapping("/isBa")
    public boolean isBa(String pid){
        if(getPidNode(pid).equals("备案"))
            return true;
        return false;
    }

    //获取备案项目
    @RequestMapping("/getBaXm")
    public List<Project> getBa(String userId){
        List<Project> bas=new ArrayList<>();
        List<Project> projects=lqxm(userId);
        for(Project project:projects){
           if(isBa(project.getPid())&&isJbs(project.getPid(),userId)){//如果该项目到达备案，且经办人为自己
               bas.add(project);
           }
        }
        return bas;
    }

    //领取项目
    @RequestMapping("/lqxm")
    public List<Project> lqxm(String userId){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService=processEngine.getTaskService();
        //查询职位
        Group group=identityService.createGroupQuery().groupMember(userId).singleResult();
        //查询部门
        String did=identityService.getUserInfo(userId,"departmentId");
        //查询组下面的任务
        List<Task> tasks=taskService.createTaskQuery().taskCandidateGroup(group.getId()).list();
        System.out.println(did);
        //过滤，查询自己部门下的任务,找到后返回
        List<Project> projects=new ArrayList<>();
        if(did.equals("20190123022801622")||did.equals("20190125102616787")){//工程技术部或者办公室不用过滤
            for(Task task:tasks){
                Project project =(Project) taskService.getVariable(task.getId(),"project");
                projects.add(project);
            }
        }else{//其他部门就需要过滤
            for(Task task:tasks){
                Project project =(Project) taskService.getVariable(task.getId(),"project");
                if(project!=null&&project.getDeclarationDep().equals(dnam(did))){
                    projects.add(project);
                }
            }
        }
        return projects;
    }

    //查看申请状态
    @RequestMapping("/zt")
    public String zt(String pi) throws IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService=processEngine.getRuntimeService();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        //根据pid找流程名字
        ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceId(pi).singleResult();
        String pidName=processInstance.getProcessDefinitionKey();
        //查询流程定义
        ProcessDefinition pd=repositoryService.createProcessDefinitionQuery().processDefinitionKey(pidName).list().get(0);
        //获取bpmn模型对象
        BpmnModel model=repositoryService.getBpmnModel(pd.getId());
        //定义使用宋体
        String fontName="宋体";
        System.out.println(pi);
        //获取流程实例当前的节点,需要高亮显示
        List<String> currentActs=runtimeService.getActiveActivityIds(pi);
        //BPMN模型对象,图片类型,显示的节点
        InputStream is=processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model,"png",currentActs,new ArrayList<String>(),fontName,fontName,fontName,null,1.0);
        //将输入流转换为byte数组
        ByteArrayOutputStream bytestream=new ByteArrayOutputStream();
        int b;
        while ((b=is.read())!=-1){
            bytestream.write(b);
        }
        byte[] bs=bytestream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(bs);
        return data;
    }

    //上传附件
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String pId, String userId)  {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService=processEngine.getTaskService();
            System.out.println(pId+' '+userId+' '+file.getOriginalFilename());
            //查找当前流程的任务
            Task task=taskService.createTaskQuery().processInstanceId(pId).singleResult();
            //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
            Attachment attachment=taskService.createAttachment("",task.getId(),pId,file.getOriginalFilename(),"",file.getInputStream());
            Attachmentlink attachmentlink = new Attachmentlink();
            attachmentlink.setUserid(userId);
            attachmentlink.setAttachment(attachment.getId());
            attachmentlinkMapper.insert(attachmentlink);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //处理任务
    @RequestMapping(value = "/addComment")
    public boolean addCommnet(String pid,String userId,String comment,String varName,String value){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            System.out.println(pid+'_'+userId+'_'+comment);
            TaskService taskService=processEngine.getTaskService();
            Task task=taskService.createTaskQuery().processInstanceId(pid).singleResult();
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(),pid,comment);
            //设置任务代理人
            taskService.setAssignee(task.getId(),userId);
            taskService.setVariable(task.getId(),varName,value);
            //完成任务
            taskService.complete(task.getId());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //项目ID拿评论
    @RequestMapping(value = "/projecttocomment")
    public List<Return_Comments> projecttocomment(String pid){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService=processEngine.getTaskService();
        List<Comment> comments = taskService.getProcessInstanceComments(pid);
        List<Return_Comments> return_comments = new ArrayList<>();
        for(int i=0;i<comments.size();i++){
            if(comments.get(i).getType().equals("event")){
                comments.remove(i);
                i--;
            }
        }
        for(Comment comment : comments){
            String uid = comment.getUserId();
            System.out.println(uid);
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
    @RequestMapping(value = "/getPidNode")
    public String getPidNode(String pid){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        TaskService taskService=processEngine.getTaskService();
        Task task=taskService.createTaskQuery().processInstanceId(pid).singleResult();
        return task.getName();
    }


    //项目表插入
    @RequestMapping("/insertXm")
    public String startpapplication(@RequestBody Project pa){
        try {
            String id = IdCreate.id();
            pa.setId(id);
            projectMapper.insert(pa);
            return id;
        }catch (Exception e){
            return null;
        }
    }

    //删除
    @RequestMapping("/deletXm")
    public boolean deletXm(@RequestBody Project po){
        try {
            if(po.getPid()==null||po.getPid().equals("")){//还没有开始跑流程、可以删
                projectMapper.deleteByPrimaryKey(po);
                return true;
            }
            if(getPidNode(po.getPid()).equals("填写申请表")){//该流程跑起来了，节点在填写申请表，可删
                ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
                RuntimeService runtimeService=engine.getRuntimeService();
                //删流程
                runtimeService.deleteProcessInstance(po.getPid(),"");
                //删表
                projectMapper.deleteByPrimaryKey(po);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    //确定申请人
    @RequestMapping("/qdsqr")
    public boolean qdsqr(String projectId,String userId){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        String username = user.getFirstName();
        Project project = new Project();
        project.setId(projectId);
        Project po = projectMapper.selectOne(project);
        String peic = po.getProposer();
        System.out.println(peic);
        System.out.println(username);
        if(username.equals(peic)){
            return true;
        }else{
            return false;
        }
    }

    //修改项目表单
    @RequestMapping("/updataXm")
    public boolean xgxmbd(@RequestBody Project po){
        try{
            if(po.getPid()==null||po.getPid().equals("")) {//没有pid，还没开始审批，可以修改
                projectMapper.updateByPrimaryKeySelective(po);
                return true;
            }
            if(getPidNode(po.getPid()).equals("填写申请表")){ //或者 该项目流程在填写审批表
                ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
                TaskService taskService=engine.getTaskService();
                Task task=taskService.createTaskQuery().processInstanceId(po.getPid()).singleResult();
                //重新设置项目参数
                taskService.setVariable(task.getId(),"project",po);
                //修改项目表
                projectMapper.updateByPrimaryKeySelective(po);
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }
    }

    //拿所有项目id和项目name
    @RequestMapping("/getAllXmIdAndXmname")
    public List<Xm> getAllXmIdAndXmname(){
        List<Project> projects=projectMapper.selectAll();
        List<Xm> xms=new ArrayList<>();
        for(Project project:projects){
            Xm xm=new Xm();
            xm.value=project.getId();
            xm.label=project.getProjectNam();
            xms.add(xm);
        }
        return xms;
    }
}
