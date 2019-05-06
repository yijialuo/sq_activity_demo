package com.sq.demo.utils;


import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.TaskService;


public class TaskName {
    public static String pidToTaskName (String pid){
        if(pid!=null&&!pid.equals("")){
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            return taskService.createTaskQuery().processInstanceId(pid).singleResult().getName();
        }else {
            return "";
        }
    }
}
