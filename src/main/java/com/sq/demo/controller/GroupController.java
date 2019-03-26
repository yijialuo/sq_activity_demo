package com.sq.demo.controller;

import com.sq.demo.utils.IdCreate;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yijialuo on 2019/1/12.
 */


@RestController
@RequestMapping("/group")
public class GroupController {

    @Transactional
    @RequestMapping("/addgroup")
    public Group addgroup(String groupName,String groupCode){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        try {
            Group group=identityService.newGroup(IdCreate.id());
            group.setName(groupName);
            group.setType(groupCode);
            identityService.saveGroup(group);
            return group;
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping("/getallgroup")
    public List<Group> getallroup(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        return identityService.createGroupQuery().list();
    }

    @Transactional
    @RequestMapping("/updatagroup")
    public boolean updatagroup(String id,String name,String code){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        try {
            Group group=identityService.createGroupQuery().groupId(id).singleResult();
            group.setName(name);
            group.setType(code);
            identityService.saveGroup(group);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
