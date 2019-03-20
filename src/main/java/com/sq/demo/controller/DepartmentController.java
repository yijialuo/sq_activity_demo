package com.sq.demo.controller;

import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.pojo.Department;
import com.sq.demo.utils.IdCreate;
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
 * Created by yijialuo on 2019/1/15.
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentMapper departmentMapper;

    //查询所有部门
    @RequestMapping("/getAllDepartment")
    List<Department> getDepartment() {
        try {
            List<Department> departments = departmentMapper.selectAll();
            List<Department> res=new ArrayList<>();
            for(Department department : departments){
                if(!department.getId().equals("0")){//过滤无部门和工程技术部&&!department.getId().equals("20190123022801622")&&!department.getId().equals("20190125102616787")
                    res.add(department);
                    continue;
                }
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    //保存department
    @RequestMapping("/insertDepartment")
    Department insertDepartment(@RequestBody Department department) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            department.setId(IdCreate.id());
            departmentMapper.insert(department);
            return department;
        } catch (Exception e) {
            return null;
        }
    }

    //修改department
    @RequestMapping("/updataDepartment")
    boolean updataDepartment(@RequestBody Department department) {
        try {
            departmentMapper.updateByPrimaryKey(department);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //id返回名字
    @RequestMapping("/IdToName")
    String IdToName(String departmentId){
        try {
            Department department = new Department();
            department.setId(departmentId);
            String name = departmentMapper.selectOne(department).getdNam();
            return name;
        } catch (Exception e) {
            return null;
        }
    }
    //删除部门
    @RequestMapping("deleteDepartment")
    public boolean deleteDepartment(String departmentId,String userId, String passWord) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        if(checkadmin(userId)){
            boolean check = identityService.checkPassword(userId, passWord);
            if(check){
                System.out.println(check);
                if(departmentId.isEmpty()){
                    return false;
                }
                Department department = new Department();
                department.setId(departmentId);
                //String tdId = departmentMapper.selectOne(department).getId();
                if(departmentMapper.selectOne(department) == null){
                    return false;
                }

                List<User> users = identityService.createUserQuery().list();
                for(User user : users){
                    String d_id = identityService.getUserInfo(user.getId(),"departmentId");
                    if(d_id.equals(departmentId)){
                        identityService.setUserInfo(user.getId(), "departmentId", "0");
                    }
                }
                departmentMapper.deleteByPrimaryKey(departmentId);
                //identityService.deleteUserInfo();
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    //确认管理员身份
    public boolean checkadmin(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        List<Group> checkgroup = identityService.createGroupQuery().groupMember(userId).list();
        for(Group group : checkgroup){
            if(group.getType().equals("00") ){
                return true;
            }
        }
        return false;
    }

}