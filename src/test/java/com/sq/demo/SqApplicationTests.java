package com.sq.demo;

import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.TestMapper;
import com.sq.demo.pojo.Department;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqApplicationTests {


    @Autowired
    TestMapper testMapper;
    @Autowired
    DepartmentMapper departmentMapper;
//    ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
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


    @Test
    public void deployxmsp(){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=engine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("bpmn/lxsp.bpmn").deploy();
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
        department.setId("66");
        department.setdNam("部门6");
        department.setdCod("6");
        departmentMapper.insert(department);
    }
    //添加职位
    @Test
    public void addposition(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=processEngine.getIdentityService();
        Group group = identityService.newGroup("4");
        group.setName("管理员");
        group.setType("d");
        identityService.saveGroup(group);
    }

    //添加用户
    @Test
    public void adduser(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        User user = identityService.newUser("444");
        user.setFirstName("ADMIN");
        user.setPassword("444");
        identityService.saveUser(user);
        identityService.setUserInfo(user.getId(),"departmentId","2");
        identityService.createMembership(user.getId(),"4");

    }

    @Test
    public void findtask(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> datas = historyService.createHistoricTaskInstanceQuery().taskAssignee("444").list();
        for(HistoricTaskInstance data : datas){
            System.out.println(data.getProcessInstanceId());
        }
        System.out.println(datas.size());
    }

    @Test
    public void addcomment(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService=processEngine.getRuntimeService();
        TaskService taskService=processEngine.getTaskService();
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("lxsp");
        Task task =taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        //taskService.saveTask(task);
        // 由于流程用户上下文对象是线程独立的，所以要在需要的位置设置，要保证设置和获取操作在同一个线程中
        Authentication.setAuthenticatedUserId("b");//批注人的名称  一定要写，不然查看的时候不知道人物信息
        // 添加批注信息
        taskService.addComment(task.getId(), pi.getId(), "this is a comment444 .");//comment为批注内容
    }
}


