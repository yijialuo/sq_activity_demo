package com.sq.demo;

import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.IdCreate;
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
import sun.misc.BASE64Encoder;

import javax.persistence.Id;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SqApplicationTests {

    @Autowired
    TbdwMapper tbdwMapper;
    @Autowired
    ContractMapper contractMapper;
    @Autowired
    ZhaobiaoMapper zhaobiaoMapper;
    @Autowired
    TestMapper testMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    AttachmentlinkMapper attachmentlinkMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ScoreMapper scoreMapper;
    @Autowired
    ContractfileMapper contractfileMapper;
    @Autowired
    PayableMapper payableMapper;
//    @Autowired
//    SupplierMapper supplierMapper;
//    ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
  //  RepositoryService repositoryService=processEngine.getRepositoryService();

    @Test
    public void deploylxsp_new1(){
        ProcessEngineConfiguration cfg=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine=cfg.buildProcessEngine();
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=engine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("bpmn/lxsp.bpmn").addClasspathResource("bpmn/zbsp.bpmn").deploy();
    }

    @Test
    public void ooo(){
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId("2515").singleResult();
        System.out.println(task.getName());
    }

   @Test
   public void testattachmentlink(){
       Attachmentlink attachmentlink=new Attachmentlink();
       attachmentlink.setAttachment("2");
       attachmentlink.setUserid("3");
       attachmentlinkMapper.insert(attachmentlink);
   }


    @Test
    public void contextLoads() {
        ProcessEngineConfiguration cfg=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        ProcessEngine processEngine=cfg.buildProcessEngine();
    }


    @Test
    public void sdfa(){
        List<Project> projects=projectMapper.selectAll();
        double zs=projects.size();
        double wx=0,wzcg=0,gdzc=0;//维修，物资采购，固定资产
        double tj=0,sb=0,wz=0,xx=0;//土建，设备，物资，信息
        for(Project project:projects){
            String type=project.getProjectType();
            if(type.equals("维修"))
                wx++;
            else if(type.equals("物资采购"))
                wzcg++;
            else
                gdzc++;
            String xmfl=project.getReviser();
            if(xmfl.equals("土建"))
                tj++;
            else if(xmfl.equals("设备"))
                sb++;
            else if(xmfl.equals("物资"))
                wz++;
            else
                xx++;
        }
        String s1=new BigDecimal(wx/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100+"";
        String s2=new BigDecimal(wzcg/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100+"";
        String s3=new BigDecimal(gdzc/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100+"";
        String s4=new BigDecimal(tj/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100+"";
        String s5=new BigDecimal(sb/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100+"";
        String s6=new BigDecimal(wz/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100+"";
        String s7=new BigDecimal(xx/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100+"";

        System.out.println(s1+" "+s2+" "+s3+" "+s4+" "+s5+" "+s6+" "+s7);
//        System.out.println(zs);
//        System.out.println(new BigDecimal(wx/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
//        System.out.println(wzcg/zs*100);
//        System.out.println(gdzc/zs*100);
//        System.out.println(tj/zs*100);
//        System.out.println(sb/zs*100);
//        System.out.println(wz/zs*100);
//        System.out.println(xx/zs*100);
//
//        System.out.println(9/2);
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
    public void updatafj(){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        TaskService taskService=engine.getTaskService();
        taskService.createAttachment("","40051","40007","1111111","","url");
    }


    @Test
    public void fileinput()  throws IOException {
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
       TaskService taskService=engine.getTaskService();
        InputStream is=taskService.getAttachmentContent("62501");

        //将输入流转换为byte数组
        ByteArrayOutputStream bytestream=new ByteArrayOutputStream();
        int b;
        while ((b=is.read())!=-1){
            bytestream.write(b);
        }
        byte[] bs=bytestream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(bs);
        System.out.println(data);
    }


    @Test
    public void sdfasdf(){
        System.out.println(projectMapper.AllCounts());
    }
    //删
    @Test
    public void deletTest(){
        com.sq.demo.pojo.Test test=new com.sq.demo.pojo.Test();
        test.setId("1");
        testMapper.deleteByPrimaryKey(test);
    }
    @Test
    public void asdfadfadsfa(){
        System.out.println(projectMapper.selectYsXm().get(0).get("ID")+" "+projectMapper.selectYsXm().get(0).get("PROJECT_NAM"));
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


    //测试技术部署
    @Test
    public void load(){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=engine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("bpmn/jsb_lxsp.bpmn").deploy();
        RuntimeService runtimeService=engine.getRuntimeService();
        ProcessInstance pi=runtimeService.startProcessInstanceByKey("jsb_lxsp");
        TaskService taskService=engine.getTaskService();
        Task task=taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        System.out.println(task.getName());
    }

    //查询pid当前任务
    @Test
    public void piddqrw(){
       ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
       TaskService taskService=engine.getTaskService();
       Task task=taskService.createTaskQuery().processInstanceId("10001").singleResult();
        System.out.println(task.getName());
    }

    //根据pid查询流程名字
    @Test
    public void pidtoname(){
       ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
       RuntimeService runtimeService=engine.getRuntimeService();
       ProcessInstance processInstance=runtimeService.createProcessInstanceQuery().processInstanceId("15001").singleResult();
        System.out.println(processInstance.getProcessDefinitionKey());
   }


    //部署
    @Test
    public void deployxmsp(){
        ProcessEngineConfiguration cfg=ProcessEngineConfiguration.createProcessEngineConfigurationFromResource("activiti.cfg.xml");
        cfg.buildProcessEngine();
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=engine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("bpmn/lxsp.bpmn").deploy();
        repositoryService.createDeployment().addClasspathResource("bpmn/jsb_lxsp.bpmn").deploy();
    }


    @Test
    public void deploylxsp3(){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        RepositoryService repositoryService=engine.getRepositoryService();
        repositoryService.createDeployment().addClasspathResource("bpmn/lxsp3.bpmn").deploy();
    }



    @Test
    public void test666(){
       ProcessEngine processEngine=ProcessEngines.getDefaultProcessEngine();
       HistoryService historyService=processEngine.getHistoryService();
       List<HistoricTaskInstance> datas=historyService.createHistoricTaskInstanceQuery().processInstanceId("12516").list();
        System.out.println(datas.get(0).getName());
   }


    @Test
    public void testlxsp3(){
       ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
       RuntimeService runtimeService=engine.getRuntimeService();
       ProcessInstance pi=runtimeService.startProcessInstanceByKey("lxsp3");
       System.out.println(pi.getName());
        System.out.println(pi.getId());//12501 20001 25001 32501 40001 45001 50001 55001
    }

    @Test
    public void test12501(){
       ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
       TaskService taskService=engine.getTaskService();
       Task task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
       taskService.setVariable(task.getId(),"cxsq",true);
       taskService.complete(task.getId());
       //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//主管经理
        //主管经理同意
        taskService.setVariable(task.getId(),"zgjl",true);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//经理
        //经理同意
        taskService.setVariable(task.getId(),"jl",0);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//经办人
        //经办人同意
//        taskService.setVariable(task.getId(),"jbrsq",1);
//        taskService.complete(task.getId());
        //经办人不同意
        taskService.setVariable(task.getId(),"jbrsq",0);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//经理
        //经理同意
        taskService.setVariable(task.getId(),"jl",0);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//经办人
        //经办人同意
        taskService.setVariable(task.getId(),"jbrsq",1);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//技术部主管
        //技术部主管同意
        taskService.setVariable(task.getId(),"jszgsq",1);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//技术部主管主管经理
        //技术部主管经理同意
        taskService.setVariable(task.getId(),"jszgjl",0);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//技术部经理
        //技术部经理同意
        taskService.setVariable(task.getId(),"jsjl",0);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//两会
        //两会同意
        taskService.setVariable(task.getId(),"lh",0);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//总经理办公会
        //总经理办公会同意
        taskService.setVariable(task.getId(),"zjl",true);
        taskService.complete(task.getId());
        //查
        task=taskService.createTaskQuery().processInstanceId("50001").singleResult();
        System.out.println(task.getName());//备案
    }


//    @Test
//    public void testsdfsafd(){
//        System.out.println(supplierMapper.selectAll().size());
//    }

    @Test
    public void insetsssss(){
       for (int i=0;i<13;i++){
           Score score=new Score();
           score.setId(IdCreate.id());
           score.setSid("2");
           score.setKhnr(i+1+"");
           score.setKhbm("项目委员会（办公室）");
           score.setPf(i+4+"");
           scoreMapper.insert(score);
       }
    }

    @Test
    public void fenye(){
        List<Project> projects = projectMapper.fenye(0,5);
        System.out.println(projects.size());
        for(Project project:projects){
            System.out.println(project.getId());
        }
    }

    @Test
    public void adsfa(){
       ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
       RepositoryService repositoryService=engine.getRepositoryService();
    }

    @Test
    public void test(){
        Contractfile contractfile=new Contractfile();
        contractfile.setCid("1");
        for(Contractfile s:contractfileMapper.select(contractfile)){
            System.out.println(s.getFname());
        }
    }

    //删除文件对应表
    @Test
    public void testsdfasdfasfd(){
        Contractfile contractfile=new Contractfile();
        contractfile.setCid("1");
        contractfileMapper.delete(contractfile);
    }

    @Test
    public void insetzzb(){
        System.out.println(IdCreate.id());
       Zhaobiao zhaobiao=new Zhaobiao();
       zhaobiao.setId(IdCreate.id());
       zhaobiao.setJsyq("jsyq");
       zhaobiao.setXmid("sadf");
       zhaobiao.setSqr("sdfdsfg");
       zhaobiao.setZbpid("ggg");
       zhaobiaoMapper.insert(zhaobiao);
    }

    @Test
    public void delesdf(){
       Zhaobiao zhaobiao=new Zhaobiao();
       zhaobiaoMapper.deleteByPrimaryKey("ae4971ce-01d4-462b-938d-3ba01c718e34");
    }

    @Test
    public void tsasdfasd(){
       ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
       TaskService taskService=engine.getTaskService();
       Task task=taskService.createTaskQuery().processInstanceId("42501").singleResult();
        System.out.println(task.getName());
    }
    @Test
    public void tasdfa(){
       List<Project> x=new ArrayList<>();
       x.add(null);
       x.add(null);
        System.out.println(x.size());
    }

    @Test
    public void tsetasd(){
        Contract contract = new Contract();
        contract.setId("4ee618c3-8647-4de8-81f2-8e79df797cdf");
        String pid = contractMapper.selectOne(contract).getProjectId();
        Project project = new Project();
        project.setId(pid);
        String pnam = projectMapper.selectOne(project).getProjectNam();
        System.out.println(pnam);
        //return pnam;
    }

    @Test
    public void asdf(){
        Tbdw tbdw=tbdwMapper.selectByPrimaryKey("1024478e-0e6c-4a2d-8d9e-918c626a4c03");
     //   System.out.println(tbdwMapper.deleteByPrimaryKey("1024478e-0e6c-4a2d-8d9e-918c626a4c03"));
        System.out.println(tbdw.getDw());
    }


    @Test
    public void testsdafasdf(){
        System.out.println(projectMapper.selectByDepartmentName("粮油操作部").size());
    }


    @Test
    public void  asdfas(){
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = engine.getRuntimeService();
        //删流程
        runtimeService.deleteProcessInstance("2505", "");
    }
}


