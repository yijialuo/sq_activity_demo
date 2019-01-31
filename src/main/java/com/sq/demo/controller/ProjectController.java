package com.sq.demo.controller;

import com.github.pagehelper.PageException;
import com.sq.demo.Entity.Project_Application;
import com.sq.demo.Entity.Project_Receive;
import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.Entity.Sqb;
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

import javax.xml.bind.attachment.AttachmentMarshaller;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.SimpleFormatter;

/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/projectApplication")
public class ProjectController {
    ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
    IdentityService identityService = processEngine.getIdentityService();

    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    AttachmentlinkMapper  attachmentlinkMapper;

    //启动申请流程,办事员填写申请表
    @RequestMapping("/startapplication")
    public String startapplication(@RequestBody Project_Receive pa){
        TaskService taskService=processEngine.getTaskService();
        RuntimeService runtimeService=processEngine.getRuntimeService();
        IdCreate idCreate = new IdCreate();
        ProcessInstance pi=runtimeService.startProcessInstanceByKey("lxsp");
        Task task=taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        String unam = identityService.createUserQuery().userId(pa.getUserId()).singleResult().getFirstName();
        Project project = new Project();
        String dnam = IdToNam(identityService.getUserInfo(pa.getUserId(),"departmentId"));
        String project_id = idCreate.id();
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
        project.setPid(pi.getId());
        projectMapper.insert(project);
        System.out.println("当前任务:" +task.getName());
        taskService.setVariable(task.getId(),"project",project);
        //设置任务受理人
        taskService.setAssignee(task.getId(),pa.getUserId());
        taskService.complete(task.getId());
        task=taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println("完成任务后的下一个任务为:" +task.getName());
        String res = project_id+'_'+pi.getId();
        return res;
    }

    public String IdToNam(String departmentId){
        Department department = new Department();
        department.setId(departmentId);
        String name = departmentMapper.selectOne(department).getdNam();
        return name;
    }

    public String getnowtime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    //id查询所有项目
    @RequestMapping("/getallproject")
    public List<Project> getallproject(String userId){
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> datas = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).list();
        List<Project> p = new ArrayList<>();
        for(HistoricTaskInstance data : datas){
            Project po = new Project();
            po.setPid(data.getProcessInstanceId());
            p.add(projectMapper.selectOne(po));
        }
        return p;
    }

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
        IdentityService identityService=processEngine.getIdentityService();
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
        if(did.equals("20190123022801622")){
            return tasks.size();
        }else{
            for(Task task:tasks){
                Project project =(Project) taskService.getVariable(task.getId(),"project");
                if(project.getDeclarationDep().equals(dnam(did))){
                    i++;
                }
            }
        }

        return i;
    }


    //领取项目
    @RequestMapping("/lqxm")
    public List<Project> lqxm(String userId){
        IdentityService identityService=processEngine.getIdentityService();
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
        if(did.equals("20190123022801622")){
            for(Task task:tasks){
                Project project =(Project) taskService.getVariable(task.getId(),"project");
                projects.add(project);
            }
        }else{
            for(Task task:tasks){
                Project project =(Project) taskService.getVariable(task.getId(),"project");
                if(project.getDeclarationDep().equals(dnam(did))){
                    projects.add(project);
                }
            }
        }
        return projects;
    }


    //查看申请状态
    @RequestMapping("/zt")
    public String zt(String pi) throws IOException {
        System.out.println("pi:"+pi);
        RuntimeService runtimeService=processEngine.getRuntimeService();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        //查询流程定义
        ProcessDefinition pd=repositoryService.createProcessDefinitionQuery().processDefinitionKey("lxsp").list().get(0);
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

    //得到用户组的申请
    @RequestMapping("/getsq")
    public List<Sqb> getSq (String userId){
        String groupId=processEngine.getIdentityService().createGroupQuery().groupMember(userId).singleResult().getId();
        List<Sqb> sqbs=new ArrayList<>();
        TaskService taskService=processEngine.getTaskService();
        //查询用户组下的任务
        List<Task> tasks=taskService.createTaskQuery().taskCandidateGroup(groupId).list();
        //得到所以任务的申请表
        for(Task task:tasks){
            sqbs.add((Sqb)taskService.getVariable(task.getId(),"sqb"));
        }
        return sqbs;
    }

    //
    @RequestMapping("/lqrw")
    public boolean lqrw(String userId,String pi){
        TaskService taskService=processEngine.getTaskService();
        Task task=taskService.createTaskQuery().processInstanceId(pi).singleResult();
        taskService.claim(task.getId(),userId);
        return true;
    }

    //上传附件
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String pId, String userId)  {
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

            //完成任务
            //taskService.complete(task.getId());
            return true;
        }catch (Exception e){
            return false;
        }
    }


    //添加评论
    @RequestMapping(value = "/addComment")
    public boolean addCommnet(String pid,String userId,String comment){
        try {
            System.out.println(pid+'_'+userId+'_'+comment);
            TaskService taskService=processEngine.getTaskService();
            Task task=taskService.createTaskQuery().processInstanceId(pid).singleResult();
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(),pid,comment);
            //设置任务代理人
            taskService.setAssignee(task.getId(),userId);
            //完成任务
            taskService.complete(task.getId());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    //项目ID拿评论
    @RequestMapping(value = "projecttocomment")
    public List<Return_Comments> projecttocomment(String pid){
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
}
