package com.sq.demo;

import com.sq.demo.Entity.Sqb;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.PositionMapper;
import com.sq.demo.pojo.Department;
import com.sq.demo.pojo.Position;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqApplicationTests {
    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    PositionMapper positionMapper;

    ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
    RepositoryService repositoryService=processEngine.getRepositoryService();
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
        createUser(identityService,"admin","管理员一","111","admin");
        createUser(identityService,"yijialuo","易家洛","111","doMan");
        createUser(identityService,"bmjl","部门经理一","111","manager");
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Deployment deployment=repositoryService.createDeployment().addClasspathResource("bpmn/sq.bpmn").deploy();
    }

    @Test
    public void do1user(){
        ProcessEngine engine= ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        createUser(identityService,"admin","管理员一","111","admin");
    }

    public void createUser(IdentityService identityService, String userId,String userName,String passWord,String groupId){
        User user=identityService.newUser(userId);
        user.setFirstName(userName);
        user.setPassword(passWord);
        identityService.saveUser(user);
        identityService.createMembership(userId,groupId);
    }

    public void createGroup(IdentityService identityService,String groupId,String groupName,String groupType){
        Group group=identityService.newGroup(groupId);
        group.setName(groupName);
        group.setType(groupType);
        identityService.saveGroup(group);
    }

    //添加流程文件
    @Test
    public void jt(){
        ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=processEngine.getRepositoryService();
        Deployment deployment=repositoryService.createDeployment().addClasspathResource("bpmn/sq.bpmn").deploy();
    }

    //查询当前用户组下的任务
    @Test
    public void dqyhz(){
        TaskService taskService=processEngine.getTaskService();
        List<Task> tasks=taskService.createTaskQuery().taskCandidateGroup("manager").list();
        System.out.println(tasks.size());
        for(Task task:tasks){
            System.out.println(task.getName());
        }
        System.out.println(((Sqb)taskService.getVariable(tasks.get(0).getId(),"sqb")).getXmfzr());
    }

    @Test
    public void TestPng() throws IOException {
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService=engine.getRuntimeService();
        RepositoryService repositoryService=engine.getRepositoryService();
        //查询流程实例
       // ProcessInstance pi=runtimeService.startProcessInstanceByKey("sp");
        //查询流程定义
        ProcessDefinition pd=repositoryService.createProcessDefinitionQuery().processDefinitionKey("sp").list().get(0);
        //获取bpmn模型对象
        BpmnModel model=repositoryService.getBpmnModel(pd.getId());

        TaskService taskService=engine.getTaskService();


        //定义使用宋体
        String fontName="宋体";
        //获取流程实例当前的节点,需要高亮显示
        List<String> currentActs=runtimeService.getActiveActivityIds("2501");
        //BPMN模型对象,图片类型,显示的节点
        InputStream is=engine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model,"png",currentActs,new ArrayList<String>(),fontName,fontName,fontName,null,1.0);


        //将输入流转换为byte数组
        ByteArrayOutputStream bytestream=new ByteArrayOutputStream();
        int b;
        while ((b=is.read())!=-1){
            bytestream.write(b);
        }
        byte[] bs=bytestream.toByteArray();


        File file = new File("sq5.png");
        FileOutputStream fop = new FileOutputStream(file);

        // if file doesnt exists, then create it
        if (!file.exists()) {
            file.createNewFile();
        }
        fop.write(bs);
        is.close();
        bytestream.close();
        fop.flush();
        fop.close();

//        int len;
//        FileOutputStream os = new FileOutputStream("static/1.png");
//        // 开始读取
//        while ((len = is.read(bs)) != -1) {
//            os.write(bs, 0, len);
//        }

    }

    //更具任务id查询pi
    @Test
    public void  pi(){
        TaskService taskService=processEngine.getTaskService();
        RuntimeService runtimeService=processEngine.getRuntimeService();
       // Task task=taskService.createTaskQuery().processInstanceId("2501").singleResult();;
        Execution exe=runtimeService.createExecutionQuery().processInstanceId("2501").singleResult();
        System.out.println(exe.getName());
    }

    @Test
    public void insert(){
        Department department=new Department();
        department.setId("2");
        department.setdNam("2");
        System.out.println(departmentMapper.insert(department));
    }

    @Test
    public void updata(){

//        Department department=new Department();
//        department.setId("20190115035538882");
//        department.setdCod("001");
//        department.setdNam("软件开发部");
//        departmentMapper.updateByPrimaryKey(department);
        Position position=new Position();
        position.setId("1");
        position.setpNam("yyyy");
        position.setpCod("pppp");
        positionMapper.updateByPrimaryKey(position);


      //  System.out.println(departmentMapper.updateByPrimaryKey(department));

//
//        Example example = new Example(Department.class);
//        Example.Criteria criteria = example.createCriteria();
//        criteria.andEqualTo("id", department.getId());
//        departmentMapper.updateByExample(department,example);
//        Department department1=new Department();
//        department1.setdCod("222sdfas");
//        department1=departmentMapper.selectOne(department1);
//        if(department1==null){
//            System.out.println("null");
//        }else {
//            System.out.println("not null");
//        }
    }
}

