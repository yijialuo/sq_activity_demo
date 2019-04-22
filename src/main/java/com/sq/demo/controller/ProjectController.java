package com.sq.demo.controller;

import com.github.pagehelper.PageHelper;
import com.sq.demo.Entity.*;
import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.Time;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.persistence.Id;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/projectApplication")
public class ProjectController {
    @Autowired
    FsMapper fsMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    DepartmentMapper departmentMapper;
    @Autowired
    AttachmentlinkMapper attachmentlinkMapper;
    @Autowired
    ZhongbiaoMapper zhongbiaoMapper;
    @Autowired
    ZhaobiaoMapper zhaobiaoMapper;
    @Autowired
    ContractfileMapper contractfileMapper;
    @Autowired
    JinduMapper jinduMapper;

    String getSgzt(String projectId) {
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (project.getFinishDte() != null && !project.getFinishDte().equals(""))
            return "已完工";
        Jindu jindu = new Jindu();
        jindu.setProjectId(projectId);
        return jinduMapper.select(jindu).size() == 0 ? "未开工" : "进行中";
    }



    //施工管理的搜索
    @RequestMapping("/sgSearch")
    public List<Project> sgSearch(String projectName, String departmentName, String fzr, String xmlb, String yjgq, String zt) {
        List<Project> projects = projectMapper.sgSearch(projectName, departmentName, fzr, xmlb, yjgq);
        if (zt == null || zt.equals("")) {
            return projects;
        } else {
            List<Project> res = new ArrayList<>();
            for (Project project : projects) {
                if (getSgzt(project.getId()).equals(zt)) {
                    res.add(project);
                }
            }
            return res;
        }
    }

    //拿到经办人的项目
    @RequestMapping("/jbrToXm")
    public List<Project> jbrToXm(String jbr) {
        Project project = new Project();
        project.setBider(jbr);
        return projectMapper.select(project);
    }

    //拿到发起人的项目
    @RequestMapping("/fqrToXm")
    public List<Project> fqrToXm(String fqr) {
        Project project = new Project();
        project.setPersonInCharge(fqr);
        return projectMapper.select(project);
    }

    //根据部门名字拿部门项目
    @RequestMapping("/selectByDepartmentName")
    public List<Project> selectByDepartmentName(int pageNum, String departmentName) {
        PageHelper.startPage(pageNum, 10);
        return projectMapper.selectByDepartmentName(departmentName);
    }


    //施工状态的搜索
    @RequestMapping("/sgztss")
    public List<Project> sgztss(String sgzt, String departmentName) {
        if (departmentName.equals("工程技术部") || departmentName.equals("办公室")) {
            if (sgzt.equals("已完工")) {
                return projectMapper.ssywgxm();
            } else if (sgzt.equals("未开工")) {
                return projectMapper.sswkgxm();
            } else {
                return projectMapper.ssjxzxm();
            }
        } else {
            if (sgzt.equals("已完工")) {
                return projectMapper.zjssywgxm(departmentName);
            } else if (sgzt.equals("未开工")) {
                return projectMapper.zjsswkgxm(departmentName);
            } else {
                return projectMapper.zjssjxzxm(departmentName);
            }
        }
    }

    //搜索项目
    @RequestMapping("search")
    public List<Project> Search(String select_xmmc, String select_code, String select_dptnm, String select_jd, @RequestParam(value = "select_xmfl[]", required = false) String[] select_xmfl, @RequestParam(value = "select_xmlb[]", required = false) String[] select_xmlb, @RequestParam(value = "select_fqr[]", required = false) String[] select_fqr, @RequestParam(value = "select_jbr[]", required = false) String[] select_jbr) {
        List<Project> list = projectMapper.search(select_code, select_xmmc, select_dptnm, select_fqr, select_jbr, select_jd, select_xmfl, select_xmlb);
        if (select_jd == null || select_jd.equals("")) {//没有节点搜索条件
            return list;
        }
        //有节点搜索条件
        List<Project> res = new ArrayList<>();
        if (select_jd.equals("未申请")) {//没有pid
            for (Project project1 : list) {
                if (project1.getPid() == null || project1.getPid().equals("")) {
                    res.add(project1);
                }
            }
            return res;
        } else {//其他
            for (Project project1 : list) {
                if (project1.getPid() != null && !project1.getPid().equals("") && getPidNode(project1.getPid()).equals(select_jd)) {
                    res.add(project1);
                }
            }
            return res; //res
        }
    }

    //项目名称和项目编号过滤
    public List<Project> xxmcAndxmbhGl(List<Project> projects, String select_xmmc, String select_code) {
        List<Project> res = new ArrayList<>();
        if (select_xmmc != null && !select_xmmc.equals("") && select_code != null && !select_code.equals("")) {//xmmc，code有东西
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).getProjectNam().contains(select_xmmc) && projects.get(i).getProjectNo() != null && projects.get(i).getProjectNo().contains(select_code)) {
                    res.add(projects.get(i));
                }
            }
        } else if (select_xmmc != null && !select_xmmc.equals("") && (select_code == null || select_code.equals(""))) {//xmmc有东西，code没东西
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).getProjectNam().contains(select_xmmc)) {
                    res.add(projects.get(i));
                }
            }
        } else if ((select_xmmc == null || select_xmmc.equals("")) && select_code != null && !select_code.equals("")) {//xmmc没东西，code有东西
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).getProjectNo() != null && projects.get(i).getProjectNo().contains(select_code)) {
                    res.add(projects.get(i));
                }
            }
        } else {//都没数据  不用过滤
            return projects;
        }
        return res;
    }

    //根据部门搜索项目
    @RequestMapping("ssXmByBm")
    public List<Project> ssXmByBm(String select_dptnm) {
        Project project = new Project();
        project.setDeclarationDep(select_dptnm);
        return projectMapper.select(project);
    }

    //根据项目类别搜索
    @RequestMapping("ssXmByXmlb")
    public List<Project> ssXmByXmlb(String select_xmlb) {
        return projectMapper.selectByXmlb(select_xmlb);
    }

    //根据项目分类搜索
    @RequestMapping("ssXmByXmfl")
    public List<Project> ssXmByXmfl(String select_xmfl) {
        return projectMapper.selectByXmfl(select_xmfl);
    }

    //根据节点搜索项目
    @RequestMapping("ssXmByJd")
    public List<Project> ssXmByJd(String select_jd) {
        if (select_jd.equals("未申请")) {
            return projectMapper.selectAllWsq();
        }
        List<Project> projects = projectMapper.selectAll();
        List<Project> res = new ArrayList<>();
        for (Project project : projects) {
            if (project.getPid() != null && !project.getPid().equals("")) {
                if (getPidNode(project.getPid()).equals(select_jd)) {
                    res.add(project);
                }
            }
        }
        return res;
    }


    //作废项目(工程技术部)
    @Transactional
    @RequestMapping("xmzf")
    public boolean xmzf(String id, String pid) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            RuntimeService runtimeService = engine.getRuntimeService();
            //删流程
            runtimeService.deleteProcessInstance(pid, "");
            //删除历史
            HistoryService historyService = engine.getHistoryService();
            historyService.deleteHistoricProcessInstance(pid);
            //删表
            projectMapper.deleteByPrimaryKey(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //拿到项目占比情况
    @RequestMapping("zb")
    public String zb() {
        List<Project> projects = projectMapper.selectAll();
        double zs = projects.size();
        if ((new Double(zs)).intValue() == 0)
            return "0-0-0-0-0-0-0-0";
        double wx = 0, wzcg = 0, gdzc = 0;//维修，物资采购，固定资产
        double tj = 0, sb = 0, wz = 0, xx = 0, lh = 0;//土建，设备，物资，信息,绿化
        for (Project project : projects) {
            String type = project.getProjectType();
            if (type.equals("维修"))
                wx++;
            else if (type.equals("物资采购"))
                wzcg++;
            else
                gdzc++;
            String xmfl = project.getReviser();
            if (xmfl.equals("土建"))
                tj++;
            else if (xmfl.equals("设备"))
                sb++;
            else if (xmfl.equals("物资"))
                wz++;
            else if (xmfl.equals("信息"))
                xx++;
            else
                lh++;
        }
        DecimalFormat df = new DecimalFormat("######0.0");
        String s1 = df.format(wx / zs * 100.00);// String.valueOf(new BigDecimal(wx/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        String s2 = df.format(wzcg / zs * 100.00);//String.valueOf(new BigDecimal(wzcg/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        String s3 = df.format(gdzc / zs * 100.00);//String.valueOf(new BigDecimal(gdzc/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        String s4 = df.format(tj / zs * 100.00);//String.valueOf(new BigDecimal(tj/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        String s5 = df.format(sb / zs * 100.00);//String.valueOf(new BigDecimal(sb/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        String s6 = df.format(wz / zs * 100.00);//String.valueOf(new BigDecimal(wz/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        String s7 = df.format(xx / zs * 100.00);//String.valueOf(new BigDecimal(xx/zs).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()*100);
        String s8 = df.format(lh / zs * 100.00);
        return s1 + "-" + s2 + "-" + s3 + "-" + s4 + "-" + s5 + "-" + s6 + "-" + s7 + "-" + s8;
    }

    //判断当前项目是否可以申请
    @RequestMapping("/isSq")
    public boolean isSq(String projectId) {
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (project.getPid() == null || project.getPid().equals(""))
            return true;
        if (getPidNode(project.getPid()).equals("填写申请表"))
            return true;
        return false;
    }

    //项目id查询项目
    @RequestMapping("/selectXmById")
    public Project selectXm(String projectId) {
        return projectMapper.selectByPrimaryKey(projectId);
    }

    //项目名称模糊搜索
    @RequestMapping("/xmmcss")
    public List<Project> xmmcss(String projectName, String declarationDep) {
        if (declarationDep.equals("工程技术部") || declarationDep.equals("办公室"))//工程技术部搜索不分部门
            return projectMapper.gcjsbxmmcss(projectName);
        return projectMapper.xmmcss(projectName, declarationDep);
    }

    //项目编号模糊搜索
    @RequestMapping("/xmbhss")
    public List<Project> xmbhss(String projectNo, String declarationDep) {
        if (declarationDep.equals("工程技术部") || declarationDep.equals("办公室"))//工程技术部搜索不分部门
            return projectMapper.gcjsbxmbhss(projectNo);
        return projectMapper.xmbhss(projectNo, declarationDep);
    }

    //  拿到有多少页
    @RequestMapping("/AllCounts")
    public int AllCounts(String dpt) {
        if (dpt == null || dpt.equals(""))
            return 0;
        if (dpt.equals("工程技术部") || dpt.equals("办公室")||dpt.equals("办公室."))
            return projectMapper.AllCounts();
        return projectMapper.selfAllCounts(dpt);
    }

    //拿自己部门项目的数量
    @RequestMapping("/selfAllCounts")
    public int selfAllCounts(String dpt) {
        return projectMapper.selfAllCounts(dpt);
    }

    //拿所有的项目
    @RequestMapping("/getAllProject")
    public List<Project> getAllProject(int pageNum, String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        String departmentId = identityService.getUserInfo(userId, "departmentId");
        if (departmentId.equals("20190123022801622") || departmentId.equals("20190125102616787")||departmentId.equals("103a990b-a59a-40bc-8ac9-a505076ca0ae")) {//工程技术部或者办公室拿所有
            PageHelper.startPage(pageNum, 10);
            return projectMapper.selectAll();
        }
        String departmentName = departmentMapper.selectByPrimaryKey(departmentId).getdNam();
        PageHelper.startPage(pageNum, 10);
        return projectMapper.selectByDepartmentName(departmentName);
    }

    @Transactional
    //从前期管理开始申请
    @RequestMapping("/qqglStartSq")
    public boolean qqglStartSq(@RequestBody Project project) {
        try {
            if (project.getPid() == null || project.getPid().equals("")) {//还没有流程id,第一次
                ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
                IdentityService identityService = engine.getIdentityService();
                TaskService taskService = engine.getTaskService();
                RuntimeService runtimeService = engine.getRuntimeService();
                //根据姓名查找申请人userId
                User user = identityService.createUserQuery().userFirstName(project.getProposer()).singleResult();
                //根据userId查找职位
                String groupName = identityService.createGroupQuery().groupMember(user.getId()).singleResult().getName();
                ProcessInstance pi = runtimeService.startProcessInstanceByKey("lxsp");
                Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                project.setPid(pi.getId());
                projectMapper.updateByPrimaryKeySelective(project);
                //设置项目参数
                taskService.setVariable(task.getId(), "project", project);
                //设置任务受理人
                taskService.setAssignee(task.getId(), user.getId());
                if (!groupName.equals("技术部办事员")) {
                    //处理前期还是申请时候的附件
                    Contractfile contractfile = new Contractfile();
                    contractfile.setCid(project.getId());
                    List<Contractfile> contractfiles = contractfileMapper.select(contractfile);
                    RepositoryService repositoryService = engine.getRepositoryService();
                    for (Contractfile contractfile1 : contractfiles) {
                        //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
                        Attachment attachment = taskService.createAttachment("", task.getId(), pi.getId(), contractfile1.getFname(), "", repositoryService.getResourceAsStream(contractfile1.getFid(), contractfile1.getFname()));
                        Attachmentlink attachmentlink = new Attachmentlink();
                        attachmentlink.setUserid(project.getProposer());
                        attachmentlink.setAttachment(attachment.getId());
                        attachmentlinkMapper.insert(attachmentlink);
                    }
                }
                //完成填写申请项目
                taskService.complete(task.getId());

                //如果申请人是技术部办事员,前面不用走,直接通过
                if (groupName.equals("技术部办事员")) {
                    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                    taskService.setVariable(task.getId(), "zgjl", true);
                    taskService.complete(task.getId());

                    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                    taskService.setVariable(task.getId(), "jl", true);
                    taskService.complete(task.getId());

                    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                    taskService.setVariable(task.getId(), "jbr", true);
                    taskService.setAssignee(task.getId(), user.getId());

                    //处理前期还是申请时候的附件
                    Contractfile contractfile = new Contractfile();
                    contractfile.setCid(project.getId());
                    List<Contractfile> contractfiles = contractfileMapper.select(contractfile);
                    RepositoryService repositoryService = engine.getRepositoryService();
                    for (Contractfile contractfile1 : contractfiles) {
                        //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
                        Attachment attachment = taskService.createAttachment("", task.getId(), pi.getId(), contractfile1.getFname(), "", repositoryService.getResourceAsStream(contractfile1.getFid(), contractfile1.getFname()));
                        Attachmentlink attachmentlink = new Attachmentlink();
                        attachmentlink.setUserid(project.getProposer());
                        attachmentlink.setAttachment(attachment.getId());
                        attachmentlinkMapper.insert(attachmentlink);
                    }

                    //插入发送表
                    Fs fs=new Fs();
                    fs.setId(IdCreate.id());
                    fs.setProjectid(project.getId());
                    UserController userController=new UserController();
                    List<UserOV> jbrs=userController.getAllJsbDoman(project.getReviser(),null);
                    String jbrss=jbrs.get(0).userId;
                    for(int i=1;i<jbrs.size();i++){
                        jbrss=jbrss+","+jbrs.get(i).userId;
                    }
                    fs.setJsbjbr(jbrss);

                    List<UserOV> jbzgjls=userController.getAllJsbZgjl(project.getReviser(),null);
                    String jbzgjlss=jbzgjls.get(0).userId;
                    for(int i=1;i<jbzgjls.size();i++){
                        jbzgjlss=jbzgjlss+","+jbzgjls.get(i).userId;
                    }
                    fs.setJsbzgjl(jbzgjlss);
                    fs.setDojsbjbr(user.getId());
                    fsMapper.insert(fs);
                    taskService.complete(task.getId());
                }
                return true;
            } else {//有流程id
                cxsq(project);
                return true;
            }
        } catch (Exception e) {
            return false;
        }

    }


    //启动申请流程,办事员填写申请表
    @Transactional
    @RequestMapping("/startApplication")
    public String startApplication(@RequestBody Project_Receive pa) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        //申请人姓名
        String unam = identityService.createUserQuery().userId(pa.getUserId()).singleResult().getFirstName();
        Project project = new Project();
        //部门名字
        String dnam = dnam(identityService.getUserInfo(pa.getUserId(), "departmentId"));
        String project_id = IdCreate.id();
        project.setProjectNo(pa.getProjectNo());
        project.setId(project_id);
        project.setProjectNam(pa.getProject_name());
        project.setProjectType(pa.getProject_type());
        project.setPersonInCharge(pa.getPerson_in_charge());
        project.setProposer(unam);
        project.setApplicationDte(getnowtime());
        project.setInvestmentEstimate(pa.getInvestment_establish());
        project.setEstablishReason(pa.getEstablish_reason());
        project.setScale(pa.getScale());
        project.setIllustration(pa.getIllustration());
        project.setDeclarationDep(dnam);
        project.setEngTechAuditOpinion(Time.getNow());
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //根据userId查找职位
        String groupName = identityService.createGroupQuery().groupMember(pa.getUserId()).singleResult().getName();
        String res;
        ProcessInstance pi;
        if (groupName.equals("办事员")) {//其他部门项目
            pi = runtimeService.startProcessInstanceByKey("lxsp");
        } else {//技术部项目
            pi = runtimeService.startProcessInstanceByKey("jsb_lxsp");
        }
        Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
        project.setPid(pi.getId());
        projectMapper.insert(project);
        //设置项目参数
        taskService.setVariable(task.getId(), "project", project);
        //设置任务受理人
        taskService.setAssignee(task.getId(), pa.getUserId());
        //完成填写申请项目
        taskService.complete(task.getId());
        res = project_id + '_' + pi.getId();
        return res;
    }

    //技术部经办人修改项目信息
    @Transactional
    @RequestMapping("/jsbdomanXgxm")
    public void jsbdomanXgxm(@RequestBody Project project) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(project.getPid()).singleResult();
        //重新设置项目参数
        taskService.setVariable(task.getId(), "project", project);
        //修改项目表
        projectMapper.updateByPrimaryKeySelective(project);
    }

    //重新申请
    @Transactional
    @RequestMapping("/cxsq")
    public void cxsq(@RequestBody Project project) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(project.getPid()).singleResult();
        //重新设置项目参数
        taskService.setVariable(task.getId(), "project", project);
        //修改项目表
        projectMapper.updateByPrimaryKeySelective(project);
        taskService.complete(task.getId());
    }

    //处理节点
    @Transactional
    @RequestMapping("/doNode")
    public boolean doNode(String pid, String userId, String comment, String varName, String value,@RequestParam(value = "peoples[]", required = false) String[] peoples) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        try {
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
            IdentityService identityService = processEngine.getIdentityService();
            Group group=identityService.createGroupQuery().groupMember(userId).singleResult();
            Project project2=new Project();
            project2.setPid(pid);
            project2=projectMapper.selectOne(project2);
            //立项部门经理处理顺利项目(peoples是技术部经办人)
            if(group.getId().equals("jl")&&value.equals("true")){
                UserController userController=new UserController();
                //拿到该项目类型的所有技术部经办人
                List<UserOV> userOVS=userController.getAllJsbDoman(project2.getReviser(),null);
                String  jbrs=userOVS.get(0).userId;
                for(int i=1;i<userOVS.size();i++){
                    jbrs=jbrs+","+userOVS.get(i).userId;
                }
                Fs fs=new Fs();
                fs.setProjectid(project2.getId());
                fs=fsMapper.selectOne(fs);
                //没有数据插入
                if(fs==null){
                    fs=new Fs();
                    fs.setProjectid(project2.getId());
                    fs.setId(IdCreate.id());
                    fs.setJsbjbr(jbrs);
                    fsMapper.insert(fs);
                }else {//有数据就更新
                    fs.setJsbjbr(jbrs);
                    fsMapper.updateByPrimaryKeySelective(fs);
                }

            }

            if (group.getId().equals("jsb_doman")) {//处理人为技术部办事人,设置经办人
                Project project = (Project) taskService.getVariable(task.getId(), "project");
                //设置经办人
                project.setBider(identityService.createUserQuery().userId(userId).singleResult().getFirstName());
                //重新设置项目参数
                taskService.setVariable(task.getId(), "project", project);
                //修改项目表
                projectMapper.updateByPrimaryKeySelective(project);
                Fs fs=new Fs();
                fs.setProjectid(project.getId());
                fs=fsMapper.selectOne(fs);
                if(fs==null){
                    fs=new Fs();
                    fs.setId(IdCreate.id());
                    fs.setProjectid(project.getId());
                    fsMapper.insert(fs);
                }
                fs.setDojsbjbr(userId);
                //!isReject(project.getId())&&
                if(value.equals("true")){//同意顺流项目
                    //修改发送表的技术部经办人和选择的技术部主管经理
                    String jsbzgjls=peoples[0];
                    for(int i=1;i<peoples.length;i++){
                        jsbzgjls=jsbzgjls+","+peoples[i];
                    }
                    fs.setJsbzgjl(jsbzgjls);
                }
                fsMapper.updateByPrimaryKeySelective(fs);

            }

            //如果技术部主管经理处理，更新发送表，设置项目主管经理处理人
            if(group.getId().equals("jsb_zgjl")){
                Fs fs=new Fs();
                fs.setProjectid(project2.getId());
                fs=fsMapper.selectOne(fs);
                if(fs==null){
                    fs=new Fs();
                    fs.setId(IdCreate.id());
                    fs.setProjectid(project2.getId());
                    fsMapper.insert(fs);
                }
                fs.setDojsbzgjl(userId);
                fsMapper.updateByPrimaryKeySelective(fs);
            }

            if (comment == null)
                comment = "";
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), pid, comment);

            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            //设置参数
            taskService.setVariable(task.getId(), varName, value);
            //完成任务
            taskService.complete(task.getId());
            return true;
//        } catch (Exception e) {
//            return false;
//        }
    }

    //完成备案
    @Transactional
    @RequestMapping("/wcba")
    public void wcba(String pid, String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
        //设置任务代理人
        taskService.setAssignee(task.getId(), userId);
        //完成任务
        taskService.complete(task.getId());
    }

    //判断当前流程的经办人
    public boolean isJbs(String pid, String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = engine.getHistoryService();
        List<HistoricTaskInstance> datas = historyService.createHistoricTaskInstanceQuery().processInstanceId(pid).list();
        for (HistoricTaskInstance data : datas) {
            if (data.getName().equals("经办人") && data.getAssignee().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    //当前时间
    public String getnowtime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }

    //id查询所有项目 根据用户id拿到自己参与的项目
    @RequestMapping("/getallproject")
    public List<Project> getallproject(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricTaskInstance> datas = historyService.createHistoricTaskInstanceQuery().taskAssignee(userId).list();
        List<Project> p = new ArrayList<>();
        for (HistoricTaskInstance data : datas) {
            Project po = new Project();
            po.setPid(data.getProcessInstanceId());
            po = projectMapper.selectOne(po);
            if (po != null)
                p.add(po);
        }
        List<Project> res = new ArrayList<>();
        if (p.size() != 0)
            res.add(p.get(0));
        for (int i = 1; i < p.size(); i++) {
            boolean c = true;
            if (p.get(i) != null) {
                for (int j = 0; j < res.size(); j++) {
                    if (p.get(i).getPid().equals(res.get(j).getPid())) {
                        c = false;
                        break;
                    }
                }
                if (c)
                    res.add(p.get(i));
            }
        }
        return res;
    }

    //部门id转name
    public String dnam(String departmentId) {
        Department department = new Department();
        department.setId(departmentId);
        String name = departmentMapper.selectOne(department).getdNam();
        return name;
    }

    //部门id查项目
    @RequestMapping("/didtoproject")
    public List<Project> didtoproject(String departmentId) {
        String dnam = dnam(departmentId);
        Project project = new Project();
        project.setDeclarationDep(dnam);
        List<Project> po = projectMapper.select(project);
        return po;
    }

    //判断改项目有没有达到备案
    @RequestMapping("/isBa")
    public boolean isBa(String pid) {
        if (getPidNode(pid).equals("备案"))
            return true;
        return false;
    }

    //获取备案项目
    @RequestMapping("/getBaXm")
    public List<Project> getBa(String userId) {
        List<Project> bas = new ArrayList<>();
        List<Project> projects = lqxm(userId);
        for (Project project : projects) {
            if (isBa(project.getPid()) && isJbs(project.getPid(), userId)) {//如果该项目到达备案，且经办人为自己
                bas.add(project);
            }
        }
        return bas;
    }

    //领取项目
    @RequestMapping("/lqxm")
    public List<Project> lqxm(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        //查询职位
        Group group = identityService.createGroupQuery().groupMember(userId).singleResult();
        //查询部门
        String did = identityService.getUserInfo(userId, "departmentId");
        //查询组下面的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(group.getId()).list();
        //过滤，查询自己部门下的任务,找到后返回
        List<Project> projects = new ArrayList<>();
        if (did.equals("20190123022801622") ) {//工程技术部
            for (Task task : tasks) {
                Project project = (Project) taskService.getVariable (task.getId(), "project");
                if (project != null)
                    projects.add(project);
            }
        } else if( did.equals("20190125102616787")){//办公室拿两会
            for (Task task : tasks) {
                Project project = (Project) taskService.getVariable (task.getId(), "project");
                if (project != null&&getPidNode(project.getPid()).equals("两会"))
                    projects.add(project);
            }
        } else {//其他部门就需要过滤
            for (Task task : tasks) {
                Project project = (Project) taskService.getVariable(task.getId(), "project");
                if (project != null && project.getDeclarationDep().equals(dnam(did))) {
                    projects.add(project);
                }
            }
        }
        //如果是办事员，拿到项目，判定是不是自己的申请人，
        if (group.getName().equals("办事员")) {
            List<Project> res = new ArrayList<>();
            //用户名
            String userName = identityService.createUserQuery().userId(userId).singleResult().getFirstName();
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).getProposer().equals(userName)) {
                    res.add(projects.get(i));
                }
            }
            //如果用户是邓博，李泽恩拿到总经会，
            if(userId.equals("db")||userId.equals("lze")){
                //查询办公室下面的任务
                List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup("bgs").list();
                for (Task task:tasks2){
                    Project project = (Project) taskService.getVariable(task.getId(), "project");
                    if (project != null &&project.getPid()!=null&&!project.getPid().equals("")&&getPidNode(project.getPid()).equals("总经理办公会")) {
                        res.add(project);
                    }
                }
            }
            return res;
        }
        //技术部办事员领取项目
        if(group.getId().equals("jsb_doman")){
            List<Project> res =new ArrayList<>();
            for(Project project:projects){
                Fs fs=new Fs();
                fs.setProjectid(project.getId());
                fs=fsMapper.selectOne(fs);
                //以前的没有数据，都发送
                if(fs==null){
                    res.add(project);
                    continue;
                }
                if(isReject(project.getId())){//如果是驳回项目，判断发送表中的dojsbjbr是不是自己
                    if(fs.getDojsbjbr()==null||fs.getDojsbjbr().equals("")||fs.getDojsbjbr().equals(userId)){
                        res.add(project);
                    }
                }else {//如果是顺流项目，判断发送表中的jsbjbr有没有自己
                    String[] jbrs=fs.getJsbjbr().split(",");
                    for (int i=0;i<jbrs.length;i++){
                        if(jbrs[i].equals(userId)){
                            res.add(project);
                            break;
                        }
                    }
                }
            }
            return res;
        }
        //技术部主管经理领取项目
        if(group.getId().equals("jsb_zgjl")){
            List<Project> res =new ArrayList<>();
            for(Project project:projects){
                Fs fs=new Fs();
                fs.setProjectid(project.getId());
                fs=fsMapper.selectOne(fs);
                //以前的没有数据，都发送
                if(fs==null){
                    res.add(project);
                    continue;
                }
                if(isReject(project.getId())){//如果是驳回项目，判断发送表中的dojsbzgjl是不是自己
                    if(fs.getDojsbzgjl()==null||fs.getDojsbzgjl().equals("")||fs.getDojsbzgjl().equals(userId)){
                        res.add(project);
                    }
                }else {//如果是顺流项目，判断发送表中的jsbzgjl有没有自己
                    if(fs.getJsbzgjl()==null||fs.getJsbzgjl().equals("")){
                        res.add(project);
                        continue;
                    }
                    String[] jsbzgjls=fs.getJsbzgjl().split(",");
                    for (int i=0;i<jsbzgjls.length;i++){
                        if(jsbzgjls[i].equals(userId)){
                            res.add(project);
                            break;
                        }
                    }
                }
            }
            return res;
        }

        return projects;
    }

    //查看申请状态
    @RequestMapping("/zt")
    public String zt(String pi) throws IOException {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //根据pid找流程名字
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(pi).singleResult();
        String pidName = processInstance.getProcessDefinitionKey();
        //查询流程定义
        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(pidName).list().get(0);
        //获取bpmn模型对象
        BpmnModel model = repositoryService.getBpmnModel(pd.getId());
        //定义使用宋体
        String fontName = "AR PL UMing HK";
        //获取流程实例当前的节点,需要高亮显示
        List<String> currentActs = runtimeService.getActiveActivityIds(pi);
        //BPMN模型对象,图片类型,显示的节点
        InputStream is = processEngine.getProcessEngineConfiguration()
                .getProcessDiagramGenerator()
                .generateDiagram(model, "png", currentActs, new ArrayList<String>(), fontName, fontName, fontName, null, 1.0);

        //将输入流转换为byte数组
        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        int b;
        while ((b = is.read()) != -1) {
            bytestream.write(b);
        }
        byte[] bs = bytestream.toByteArray();
        BASE64Encoder encoder = new BASE64Encoder();
        String data = encoder.encode(bs);
        return data;
    }

    //上传附件
    @Transactional
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String pId, String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            //查找当前流程的任务
            Task task = taskService.createTaskQuery().processInstanceId(pId).singleResult();
            //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
            Attachment attachment = taskService.createAttachment("", task.getId(), pId, file.getOriginalFilename(), "", file.getInputStream());
            Attachmentlink attachmentlink = new Attachmentlink();
            attachmentlink.setUserid(userId);
            attachmentlink.setAttachment(attachment.getId());
            attachmentlinkMapper.insert(attachmentlink);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //处理任务
    @Transactional
    @RequestMapping(value = "/addComment")
    public boolean addCommnet(String pid, String userId, String comment, String varName, String value) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), pid, comment);
            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            taskService.setVariable(task.getId(), varName, value);
            //完成任务
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //判断项目是否是驳回项目
    @RequestMapping(value = "/isReject")
    public boolean isReject(String projectId) {
        Project project = projectMapper.selectByPrimaryKey(projectId);
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        List<Comment> comments = taskService.getProcessInstanceComments(project.getPid());
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getType().equals("event")) {
                comments.remove(i);
                i--;
            }
        }
        if (comments.size() != 0) {
            if(comments.get(0).getFullMessage().length()>=3){
                if (comments.get(0).getFullMessage().substring(0, 3).equals("驳回：")) {
                    return true;
                } else {
                    return false;
                }
            }else {
                return false;
            }

        }
        return false;
    }

    //pid拿评论
    @RequestMapping(value = "/projecttocomment")
    public List<Return_Comments> projecttocomment(String pid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        List<Comment> comments = taskService.getProcessInstanceComments(pid);
        List<Return_Comments> return_comments = new ArrayList<>();
        for (int i = 0; i < comments.size(); i++) {
            if (comments.get(i).getType().equals("event")) {
                comments.remove(i);
                i--;
            }
        }
        for (Comment comment : comments) {
            String uid = comment.getUserId();
            User user = identityService.createUserQuery().userId(uid).singleResult();
            String unam = user.getFirstName();
            Return_Comments return_comments1 = new Return_Comments();
            return_comments1.setComment(comment.getFullMessage());
            return_comments1.setUsernam(unam);
            String dd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getTime());
            return_comments1.setTime(dd);
            return_comments.add(return_comments1);
        }
        return return_comments;
    }

    //拿流程的当前节点
    @RequestMapping(value = "/getPidNode")
    public String getPidNode(String pid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
        return task.getName();
    }

    //项目表插入
    @Transactional
    @RequestMapping("/insertXm")
    public String startpapplication(@RequestBody Project pa) {
        try {
            String id = IdCreate.id();
            pa.setId(id);
            pa.setEngTechAuditOpinion(Time.getNow());
            projectMapper.insert(pa);
            return id;
        } catch (Exception e) {
            return null;
        }
    }

    //判断项目是否可删
    boolean canDelet(String projectId) {
        List<String> xmids = projectMapper.getGlXmid();//关联表的xmid;
        for (String xmid : xmids) {
            if (xmid.equals(projectId)) //有关联数据,不可删除
                return false;
        }

        Project project = projectMapper.selectByPrimaryKey(projectId);

        if (project.getPid() != null && !project.getPid().equals("") && !getPidNode(project.getPid()).equals("填写申请表"))//有pid
            return false;

        return true;
    }

    //删除
    @Transactional
    @RequestMapping("/deletXm")
    public boolean deletXm(@RequestBody Project po) {
        if (canDelet(po.getId())) {
            projectMapper.delete(po);
            if (po.getPid() != null && !po.getPid().equals("")) {
                ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
                RuntimeService runtimeService = engine.getRuntimeService();
                //删流程
                runtimeService.deleteProcessInstance(po.getPid(), "");
                //删除历史
                HistoryService historyService = engine.getHistoryService();
                historyService.deleteHistoricProcessInstance(po.getPid());
            }
            return true;
        }
        return false;
    }

    //项目id拿到项目申请部门
    @RequestMapping("/xmidTosqbm")
    public String xmidTosqbm(String xmid) {
        return projectMapper.selectByPrimaryKey(xmid).getDeclarationDep();
    }

    //确定申请人(确定修改按钮的显示)
    @RequestMapping("/qdsqr")
    public boolean qdsqr(String projectId, String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        String username = user.getFirstName();
        Project project = new Project();
        project.setId(projectId);
        Project po = projectMapper.selectOne(project);
        String peic = po.getProposer();
        if (po.getPid() == null || po.getPid().equals("")) {
            if (username.equals(peic)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (getPidNode(po.getPid()).equals("填写申请表")) {
                if (username.equals(peic)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    //修改项目表单的项目号
    @Transactional
    @RequestMapping("/xgxmbh")
    public boolean xgxmbh(String xmid, String xmbh) {
        try {
            Project project = projectMapper.selectByPrimaryKey(xmid);
            project.setProjectNo(xmbh);
            //修改项目表
            projectMapper.updateByPrimaryKeySelective(project);
            //修改流程项目
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(project.getPid()).singleResult();
            //重新设置项目参数
            taskService.setVariable(task.getId(), "project", project);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //修改项目表单
    @Transactional
    @RequestMapping("/updataXm")
    public boolean xgxmbd(@RequestBody Project po) {
        try {
            if (po.getPid() == null || po.getPid().equals("")) {//没有pid，还没开始审批，可以修改
                projectMapper.updateByPrimaryKeySelective(po);
                return true;
            }
            if (getPidNode(po.getPid()).equals("填写申请表")) { //或者 该项目流程在填写审批表
                ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
                TaskService taskService = engine.getTaskService();
                Task task = taskService.createTaskQuery().processInstanceId(po.getPid()).singleResult();
                //重新设置项目参数
                taskService.setVariable(task.getId(), "project", po);
                //修改项目表
                projectMapper.updateByPrimaryKeySelective(po);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    //拿到本部门未开始招标的项目id和项目name,且项目流程得走到两会才行
    @RequestMapping("/getSelfWzzXmidAndXmname")
    public List<Xm> getSelfWzzXmidAndXmname(String dpt) {
        List<Xm> xms = getSelfXmidAndXmname(dpt);
        //所有招标了的项目
        List<String> projectIds = new ArrayList<>();
        for (Zhaobiao zhaobiao : zhaobiaoMapper.selectAll()) {
            projectIds.add(zhaobiao.getXmid());
        }
        List<Xm> res = new ArrayList<>();
        for (Xm xm : xms) {
            Project project = projectMapper.selectByPrimaryKey(xm.value);
            if (project.getPid() != null && !project.getPid().equals("")) {
                String node = getPidNode(project.getPid());
                if (!projectIds.contains(xm.value) && (node.equals("两会") || node.equals("招标结束") || node.equals("总经理办公会") || node.equals("备案"))) {
                    res.add(xm);
                }
            }
        }
        return res;
    }

    //拿到本部门项目id和项目name
    @RequestMapping("/getSelfXmidAndXmname")
    public List<Xm> getSelfXmidAndXmname(String dpt) {
        List<Project> projects = projectMapper.selectAll();
        List<Xm> xms = new ArrayList<>();
        for (Project project : projects) {
            if (project.getDeclarationDep().equals(dpt)) {
                Xm xm = new Xm();
                xm.value = project.getId();
                xm.label = project.getProjectNam();
                xms.add(xm);
            }

        }
        return xms;
    }

    //拿所有可以新建合同得项目id和项目name
    @RequestMapping("/getCanHtXmIdAndXmname")
    public List<Xm> getCanHtXmIdAndXmname() {
        List<Project> projects = projectMapper.selectAll();
        List<Xm> xms = new ArrayList<>();
        for (Project project : projects) {
            Zhaobiao zhaobiao = new Zhaobiao();
            zhaobiao.setXmid(project.getId());
            zhaobiao = zhaobiaoMapper.selectOne(zhaobiao);
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            if (zhaobiao != null && zhaobiao.getZbpid() != null && !zhaobiao.getZbpid().equals("")) {
                String nodeName = taskService.createTaskQuery().processInstanceId(zhaobiao.getZbpid()).singleResult().getName();
                if (nodeName.equals("定标") || nodeName.equals("招标结束")) {
                    Xm xm = new Xm();
                    xm.value = project.getId();
                    xm.label = project.getProjectNam();
                    xms.add(xm);
                }
            }
        }
        return xms;
    }


    //拿所有项目id和项目name
    @RequestMapping("/getAllXmIdAndXmname")
    public List<Xm> getAllXmIdAndXmname() {
        List<Project> projects = projectMapper.selectAll();
        List<Xm> xms = new ArrayList<>();
        for (Project project : projects) {
            Xm xm = new Xm();
            xm.value = project.getId();
            xm.label = project.getProjectNam();
            xms.add(xm);
        }
        return xms;
    }

    //拿到还可以验收的项目id和项目name
    @RequestMapping("/selectYsXm")
    public List<Xm> selectYsXm() {
        List<Map> xms = projectMapper.selectYsXm();
        List<Xm> xmList = new ArrayList<>();
        for (int i = 0; i < xms.size(); i++) {
            Xm xm = new Xm();
            xm.value = xms.get(i).get("ID").toString();
            xm.label = xms.get(i).get("PROJECT_NAM").toString();
            xmList.add(xm);
        }
        return xmList;
    }

    @RequestMapping("/xmIdToxmNo")
    public String xmIdToxmNo(String xmId) {
        return projectMapper.selectByPrimaryKey(xmId).getProjectNo();
    }

    //项目id拿项目name
    @RequestMapping("/xmIdToxmName")
    public String xmIdToxmName(String xmId) {
        return projectMapper.selectByPrimaryKey(xmId).getProjectNam();
    }


}
