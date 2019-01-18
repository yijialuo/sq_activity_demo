package com.sq.demo;

import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.TestMapper;
import com.sq.demo.pojo.Department;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqApplicationTests {


    @Autowired
    TestMapper testMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
  //  RepositoryService repositoryService=processEngine.getRepositoryService();
    @Test
    public void contextLoads() {
        ProcessEngineConfiguration cfg=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine=cfg.buildProcessEngine();
    }

    //插
    @Test
    public void testTEST(){
        com.sq.demo.pojo.Test test=new com.sq.demo.pojo.Test();
        test.setId("1");
        test.setName("yijialuo");
        test.setType("hahah");
        testMapper.insert(test);
    }
    //删
    @Test
    public void deletTest(){
        com.sq.demo.pojo.Test test=new com.sq.demo.pojo.Test();
        test.setId("1");
        testMapper.deleteByPrimaryKey(test);
    }

    //改
    @Test
    public void updata(){
        com.sq.demo.pojo.Test test=new com.sq.demo.pojo.Test();
        test.setId("1");
        test.setName("fengyu");
        testMapper.updateByPrimaryKeySelective(test);
    }

    //查
    @Test
    public void cha(){
        com.sq.demo.pojo.Test test=new com.sq.demo.pojo.Test();
        test.setId("1");
        System.out.println(testMapper.selectOne(test).getName());
    }

    //添加部门
    @Test
    public void adddepartment(){
        Department department = new Department();
        department.setId("4");
        department.setdNam("部门3");
        department.setdCod("c");
        departmentMapper.insert(department);
    }
    //添加职位
    @Test
    public void addposition(){
        IdentityService identityService=processEngine.getIdentityService();
        Group group = identityService.newGroup("3");
        group.setName("职位3");
        group.setType("c");
        identityService.saveGroup(group);
    }

    //添加用户
    @Test
    public void adduser(){
        IdentityService identityService = processEngine.getIdentityService();
        User user = identityService.newUser("333");
        user.setFirstName("GYJ");
        user.setPassword("333");
        identityService.saveUser(user);
        identityService.setUserInfo(user.getId(),"departmentId","2");
        identityService.createMembership(user.getId(),"3");

    }
}


