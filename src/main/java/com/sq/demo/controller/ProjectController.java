package com.sq.demo.controller;

import com.sq.demo.Entity.Project_Application;
import com.sq.demo.Entity.Sqb;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Department;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.IdCreate;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    //启动申请流程,办事员填写申请表
    @RequestMapping("/startapplication")
    public String startapplication(@RequestBody Project_Application pa){
        TaskService taskService=processEngine.getTaskService();
        RuntimeService runtimeService=processEngine.getRuntimeService();
        IdCreate idCreate = new IdCreate();
        ProcessInstance pi=runtimeService.startProcessInstanceByKey("lxsp");
        Task task=taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        Project project = new Project();
        String dnam = IdToNam(identityService.getUserInfo(pa.getUserId(),"departmentId"));
        String project_id = idCreate.id();
        project.setId(project_id);
        project.setProjectNam(pa.getProject_name());
        project.setProjectType(pa.getProject_type());
        project.setPersonInCharge(pa.getPerson_in_charge());
        project.setInvestmentEstimate(pa.getInvestment_establish());
        project.setEstablishReason(pa.getEstablish_reason());
        project.setScale(pa.getScale());
        project.setIllustration(pa.getIllustration());
        project.setDeclarationDep(dnam);
        project.setPid(pi.getId());
        projectMapper.insert(project);
        System.out.println("当前任务:" +task.getName());
        //将流程实例Id保存在sqb
        pa.setPi(pi.getId());
        taskService.setVariable(task.getId(),"lxsp",pa);
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

    //查看申请状态
    @RequestMapping("/zt")
    public String zt(String pi) throws IOException {
        System.out.println("pi:"+pi);
        RuntimeService runtimeService=processEngine.getRuntimeService();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        //查询流程定义
        ProcessDefinition pd=repositoryService.createProcessDefinitionQuery().processDefinitionKey("sp").list().get(0);
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
}
