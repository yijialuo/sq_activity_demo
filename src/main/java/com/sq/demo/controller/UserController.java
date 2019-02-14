package com.sq.demo.controller;

import com.sq.demo.Entity.UserOV;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.pojo.Department;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yijialuo on 2019/1/12.
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    DepartmentMapper departmentMapper;

    @RequestMapping("/login")
    public boolean dologin(String username, String password) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        return identityService.checkPassword(username, password);
    }

    @RequestMapping("/getuser")
    public UserOV getuser(String userId , String passWord) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        boolean check = identityService.checkPassword(userId, passWord);
        if(check){
            User user = identityService.createUserQuery().userId(userId).singleResult();
            UserOV userOV = new UserOV();
            userOV.userId = user.getId();
            userOV.userName = user.getFirstName();
            userOV.passWord = user.getPassword();
            userOV.departmentId = identityService.getUserInfo(user.getId(), "departmentId");
            //查询用户所在的组
            String groupId = identityService.createGroupQuery().groupMember(user.getId()).singleResult().getId();
            userOV.groupId = groupId;
            Department department = new Department();
            department.setId(userOV.departmentId);
            String d_name = departmentMapper.selectOne(department).getdNam();
            userOV.departmentName = d_name;
            Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
            userOV.groupName = group.getName();
            return userOV;
        }else{
            return null;
        }

    }

    @RequestMapping("/adduser")
    public boolean Adduser(@RequestBody UserOV userOV) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        User user = identityService.createUserQuery().userId(userOV.userId).singleResult();
        if (user != null)
            return false;
        user = identityService.newUser(userOV.userId);
        user.setFirstName(userOV.userName);
        user.setPassword(userOV.passWord);
        //保存用户部门
        identityService.setUserInfo(user.getId(), "departmentId", userOV.departmentId);
        identityService.saveUser(user);
        identityService.createMembership(user.getId(), userOV.groupId);
        return true;
    }

    @RequestMapping("/deleteuser")
    public boolean deleteuser(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        identityService.deleteUser(userId);
        return true;
    }

    @RequestMapping("/edituser")
    public boolean edituser(@RequestBody UserOV userOV) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        User user = identityService.createUserQuery().userId(userOV.userId).singleResult();
        user.setFirstName(userOV.userName);
        user.setPassword(userOV.passWord);
        String groupId = identityService.createGroupQuery().groupMember(userOV.userId).singleResult().getId();
        //新的groupId不等于原来的就修改
        if (!groupId.equals(userOV.groupId)) {
            identityService.deleteMembership(userOV.userId, groupId);
            identityService.createMembership(user.getId(), userOV.groupId);
        }
        //修改部门ID
        identityService.setUserInfo(user.getId(), "departmentId", userOV.departmentId);
        identityService.saveUser(user);
        return true;
    }

    @RequestMapping("getallusers")
    public List<UserOV> getallusers(String userId, String passWord) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        List<UserOV> userOVs = new ArrayList<>();
        List<User> users = identityService.createUserQuery().list();
        List<Group> checkgroup = identityService.createGroupQuery().groupMember(userId).list();
        for(Group group : checkgroup){
            if(group.getId().equals("admin") ){
                boolean check = identityService.checkPassword(userId, passWord);
                if (check) {
                    for (User user : users) {
                        UserOV userOV = new UserOV();
                        userOV.userId = user.getId();
                        userOV.userName = user.getFirstName();
                        userOV.passWord = user.getPassword();
                        userOV.departmentId = identityService.getUserInfo(user.getId(), "departmentId");
                        //查询用户所在的组
                        String groupId = identityService.createGroupQuery().groupMember(user.getId()).singleResult().getId();
                        userOV.groupId = groupId;
                        Department department = new Department();
                        department.setId(userOV.departmentId);
                        System.out.println(userOV.departmentId);
                        userOV.departmentName = departmentMapper.selectOne(department).getdNam();
                        Group group1 = identityService.createGroupQuery().groupId(groupId).singleResult();
                        userOV.groupName = group1.getName();
                        userOVs.add(userOV);
                    }
                    return userOVs;
                }else{
                    return null;
                }
            }else{
                continue;
            }
        }
        return null;
    }

    //确认管理员身份
    @RequestMapping("checkadmin")
    public boolean checkadmin(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        List<Group> checkgroup = identityService.createGroupQuery().groupMember(userId).list();
        for(Group group : checkgroup){
            if(group.getId().equals("admin") ){
                return true;
            }
        }
        return false;
    }
}
