package com.sq.demo.controller;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("process")
public class ProcessController {

    @RequestMapping("getJd")
    public List<String> getJd(String lc) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService = engine.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(lc).latestVersion().singleResult();
        BpmnModel model = repositoryService.getBpmnModel(processDefinition.getId());
        List<String> res = new ArrayList<>();
        if (model != null) {
            Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
            for (FlowElement e : flowElements) {
                if (e instanceof UserTask)
                    if (!e.getName().contains("结束"))
                        res.add(e.getName());
            }
        }
        return res;
    }
}
