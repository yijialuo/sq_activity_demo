package com.sq.demo.controller;


import com.sq.demo.mapper.ZhaobiaoMapper;
import com.sq.demo.pojo.Zhaobiao;
import com.sq.demo.utils.IdCreate;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/zhaobiao")
public class ZhaobiaoController {
    @Autowired
    ZhaobiaoMapper zhaobiaoMapper;

    //启动招标申请流程,办事员填写招标表
    @RequestMapping("/startZhaobiao")
    public String startZhaobiao(@RequestBody Zhaobiao zhaobiao){
        zhaobiao.setId(IdCreate.id());
        ProcessEngine engine= ProcessEngines.getDefaultProcessEngine();
        TaskService taskService=engine.getTaskService();
        RuntimeService runtimeService=engine.getRuntimeService();
        ProcessInstance pi=runtimeService.startProcessInstanceByKey("ztb");
        zhaobiao.setZbpid(pi.getId());
        zhaobiaoMapper.insert(zhaobiao);
        Task task=taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //设置招标表参数
        taskService.setVariable(task.getId(),"zhaobiao",zhaobiao);
        //设置任务受理人
        taskService.setAssignee(task.getId(),zhaobiao.getSqr());
        //完成填写申请项目
        taskService.complete(task.getId());
        System.out.println(zhaobiao.getId()+"_"+pi.getId());
        return zhaobiao.getId()+"_"+pi.getId();
    }

    //领取招标表单

}
