package com.sq.demo.utils;

import org.activiti.engine.task.Task;

import java.util.List;

public class TaskUtil {
    public static List<Task> filterOtherLc(List<Task> tasks,String lc){
        //过滤掉不是前期的任务
        for(int i=0;i<tasks.size();i++){
            if(!tasks.get(i).getProcessDefinitionId().split(":")[0].equals(lc)){
                tasks.remove(i);
                i--;
            }
        }
        return tasks;
    }
}
