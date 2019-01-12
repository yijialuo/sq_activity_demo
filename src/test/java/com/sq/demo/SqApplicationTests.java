package com.sq.demo;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void createUsersAndGroups(){
        ProcessEngine engine= ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        createGroup(identityService,"doMan","办事员","doMan");
        createGroup(identityService,"manager","部门经理","manager");
        createGroup(identityService,"tc_director","技术部主管","tc_director");
        createGroup(identityService,"tc_manager","技术部经理","tc_manager");
        createGroup(identityService,"admin","管理员","admin");
    }

    @Test
    public void do1user(){
        ProcessEngine engine= ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        createUser(identityService,"admin","123456");
    }

    public void createUser(IdentityService identityService, String userName,String passWord){
        User user=identityService.newUser("admin");
        user.setFirstName(userName);
        user.setPassword(passWord);
        identityService.saveUser(user);
    }

    public void createGroup(IdentityService identityService,String groupId,String groupName,String groupType){
        Group group=identityService.newGroup(groupId);
        group.setName(groupName);
        group.setType(groupType);
        identityService.saveGroup(group);
    }
}

