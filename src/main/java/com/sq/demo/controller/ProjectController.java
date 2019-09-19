package com.sq.demo.controller;

import com.github.pagehelper.PageHelper;
import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.Entity.UserOV;
import com.sq.demo.Entity.Xm;
import com.sq.demo.mapper.*;
import com.sq.demo.pojo.*;
import com.sq.demo.utils.GroupUtils;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.TaskUtil;
import com.sq.demo.utils.Time;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.identity.Authentication;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by yijialuo on 2019/1/13.
 */

@RestController
@RequestMapping("/projectApplication")
public class ProjectController {
    @Autowired
    XmsjbMapper xmsjbMapper;
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
    @Autowired
    XxmglMapper xxmglMapper;
    @Autowired
    YscjdwjMapper yscjdwjMapper;
    @Autowired
    ContractMapper contractMapper;

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
            for (Project project : list)
                addDqjd(project);
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
            for (Project project : res)
                addDqjd(project);
            return res;
        } else {//其他
            for (Project project1 : list) {
                if (project1.getPid() != null && !project1.getPid().equals("") && getPidNode(project1.getPid()).equals(select_jd)) {
                    res.add(project1);
                }
            }
            for (Project project : res)
                addDqjd(project);
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
            if (canDelet(id)) {
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
            } else {
                return false;
            }
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


    //判断当前项目是否可以申请(申请按钮)
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
        if (dpt.equals("工程技术部") || dpt.equals("办公室") || dpt.equals("办公室.") || dpt.equals("领导"))
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
        //工程技术部或者办公室(办公室.)领导拿所有
        if (departmentId.equals("20190123022801622") || departmentId.equals("20190125102616787") || departmentId.equals("103a990b-a59a-40bc-8ac9-a505076ca0ae") || departmentId.equals("ba7c0e37-3df1-463d-9eda-c90bc564d6c5")) {
            PageHelper.startPage(pageNum, 10);
            List<Project> projects = projectMapper.selectAll();
            for (Project project : projects)
                addDqjd(project);
            return projects;
        }
        String departmentName = departmentMapper.selectByPrimaryKey(departmentId).getdNam();
        PageHelper.startPage(pageNum, 10);
        List<Project> projects = projectMapper.selectByDepartmentName(departmentName);
        for (Project project : projects)
            addDqjd(project);
        return projects;
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
                List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
                ProcessInstance pi = runtimeService.startProcessInstanceByKey("lxsp");
                Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                project.setPid(pi.getId());
                projectMapper.updateByPrimaryKeySelective(project);
                //设置任务受理人
                taskService.setAssignee(task.getId(), user.getId());

                //处理前期还未申请时候的附件
                Contractfile contractfile = new Contractfile();
                contractfile.setCid(project.getId());
                List<Contractfile> contractfiles = contractfileMapper.select(contractfile);
                RepositoryService repositoryService = engine.getRepositoryService();
                for (Contractfile contractfile1 : contractfiles) {
                    //上传附件  参数：附件类型、任务id，流程id，附件名称，附件描述，文件流
                    Attachment attachment = taskService.createAttachment("", task.getId(), pi.getId(), contractfile1.getFname(), "", repositoryService.getResourceAsStream(contractfile1.getFid(), contractfile1.getFname()));
                    Attachmentlink attachmentlink = new Attachmentlink();
                    attachmentlink.setUserid(user.getId());
                    attachmentlink.setAttachment(attachment.getId());
                    attachmentlinkMapper.insert(attachmentlink);
                    //更新已上传节点文件的fid
                    Yscjdwj yscjdwj = new Yscjdwj();
                    //以前是部署id，为申请的时候用部署id下载
                    yscjdwj.setFid(contractfile1.getFid());
                    yscjdwj.setFname(contractfile1.getFname());
                    yscjdwj.setJlid(project.getId());
                    yscjdwj = yscjdwjMapper.selectOne(yscjdwj);
                    //申请了用附件id下载
                    yscjdwj.setFid(attachment.getId());
                    yscjdwjMapper.updateByPrimaryKeySelective(yscjdwj);
                }

                //完成填写申请项目
                taskService.complete(task.getId());

                //如果申请人是技术部办事员,前面两步不用走,直接到达技术部经办人
                if (GroupUtils.equalsJs(groups, "jsb_doman")) {
                    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                    taskService.setVariable(task.getId(), "zgjl", true);
                    taskService.complete(task.getId());

                    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
                    taskService.setVariable(task.getId(), "jl", true);
                    taskService.complete(task.getId());

//                    task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
//                    taskService.setVariable(task.getId(), "jbr", true);
//                    taskService.setAssignee(task.getId(), user.getId());


                    //插入发送表
                    Fs fs = new Fs();
                    fs.setId(IdCreate.id());
                    fs.setProjectid(project.getId());
                    UserController userController = new UserController();
                    List<UserOV> jbrs = userController.getAllJsbDoman(project.getReviser(), null);
                    String jbrss = jbrs.get(0).userId;
                    for (int i = 1; i < jbrs.size(); i++) {
                        jbrss = jbrss + "," + jbrs.get(i).userId;
                    }
                    //发往该类型的各个技术部经办人
                    fs.setJsbjbr(jbrss);

//                    List<UserOV> jbzgjls = userController.getAllJsbZgjl(project.getReviser(), null);
//                    String jbzgjlss = jbzgjls.get(0).userId;
//                    for (int i = 1; i < jbzgjls.size(); i++) {
//                        jbzgjlss = jbzgjlss + "," + jbzgjls.get(i).userId;
//                    }
//                    fs.setJsbzgjl(jbzgjlss);
//                    fs.setDojsbjbr(user.getId());
                    fsMapper.insert(fs);
                    //taskService.complete(task.getId());
                }
                Xmsjb xmsjb = new Xmsjb();
                xmsjb.setProjectid(project.getId());
                xmsjb.setQqkssj(Time.getNow());
                xmsjbMapper.updateByPrimaryKeySelective(xmsjb);
                return true;
            } else {//有流程id
                cxsq(project);
                return true;
            }
        } catch (Exception e) {
            return false;
        }

    }


    //技术部经办人修改项目信息
    @Transactional
    @RequestMapping("/jsbdomanXgxm")
    public void jsbdomanXgxm(@RequestBody Project project) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = engine.getTaskService();
        //重新设置项目参数
        //taskService.setVariable(task.getId(), "project", project);
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
        //修改项目表
        projectMapper.updateByPrimaryKeySelective(project);
        if (!project.getDeclarationDep().equals("工程技术部"))
            taskService.complete(task.getId());
        else {//工程技术部重新申请，立项部门主管经理、经理直接同意
            taskService.complete(task.getId());
            task = taskService.createTaskQuery().processInstanceId(project.getPid()).singleResult();
            taskService.setVariable(task.getId(), "zgjl", true);
            taskService.complete(task.getId());

            task = taskService.createTaskQuery().processInstanceId(project.getPid()).singleResult();
            taskService.setVariable(task.getId(), "jl", true);
            taskService.complete(task.getId());
        }
    }

    //技术部主管经理驳回处理
    @RequestMapping("/jszgjlBh")
    public boolean jszgjlBh(String pid, String userId, String comment, String bhd) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
            if (comment == null)
                comment = "";
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), pid, comment);
            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            //设置参数
            taskService.setVariable(task.getId(), "jszgjl", false);
            //完成任务
            taskService.complete(task.getId());
            if (bhd.equals("技术部经办人")) {
                return true;
            } else {
                //技术部经办人不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置技术部主管经理驳回参数
                taskService.setVariable(task.getId(), "jbr", false);
                taskService.complete(task.getId());

                //立项部们经理不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置技术部主管经理驳回参数
                taskService.setVariable(task.getId(), "jl", false);
                taskService.complete(task.getId());

                //立项部门主管经理不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置立项部门主管经理不同意参数
                taskService.setVariable(task.getId(), "zgjl", false);
                taskService.complete(task.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //技术部经理的驳回处理
    @RequestMapping("/jsjlBh")
    public boolean jsjlBh(String pid, String userId, String comment, String bhd) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = engine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
            if (comment == null)
                comment = "";
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), pid, comment);
            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            //设置参数
            taskService.setVariable(task.getId(), "jsjl", false);
            //完成任务
            taskService.complete(task.getId());
            if (bhd.equals("技术部主管")) {
                return true;
            } else if (bhd.equals("技术部经办人")) {
                //技术主管经理的不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置技术部主管经理驳回参数
                taskService.setVariable(task.getId(), "jszgjl", false);
                taskService.complete(task.getId());
                return true;
            } else {//驳回到立项部门经办人
                //技术主管经理的不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置技术部主管经理驳回参数
                taskService.setVariable(task.getId(), "jszgjl", false);
                taskService.complete(task.getId());

                //技术部经办人不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置技术部主管经理驳回参数
                taskService.setVariable(task.getId(), "jbr", false);
                taskService.complete(task.getId());

                //立项部们经理不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置技术部主管经理驳回参数
                taskService.setVariable(task.getId(), "jl", false);
                taskService.complete(task.getId());

                //立项部门主管经理不同意
                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //设置立项部门主管经理不同意参数
                taskService.setVariable(task.getId(), "zgjl", false);
                taskService.complete(task.getId());
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    //处理节点
    @Transactional
    @RequestMapping("/doNode")
    public boolean doNode(String pid, String userId, String comment, String varName, String value, @RequestParam(value = "peoples[]", required = false) String[] peoples) {
        System.out.println(Time.getNow() + "  pid:" + pid);
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        try {
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
            IdentityService identityService = processEngine.getIdentityService();
            List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
            Project project2 = new Project();
            project2.setPid(pid);
            project2 = projectMapper.selectOne(project2);
            //总经会的驳回，邓博、李泽恩
            if (getPidNode(pid).equals("总经理办公会") && value.equals("1")) {
                if (comment == null)
                    comment = "";
                //添加评论
                Authentication.setAuthenticatedUserId(userId);
                taskService.addComment(task.getId(), pid, comment);
                //设置任务代理人
                taskService.setAssignee(task.getId(), userId);
                //设置任务代理人
                taskService.setAssignee(task.getId(), userId);
                //设置总经会驳回参数
                taskService.setVariable(task.getId(), varName, value);
                //完成任务
                taskService.complete(task.getId());
                if (project2.getDepAuditOpinion().equals("股份项目")) {//股份项目的驳回，直接退到技术部经理
                    task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                    //直接设置两会驳回参数
                    taskService.setVariable(task.getId(), "lh", 2);
                    //完成任务
                    taskService.complete(task.getId());
                    return true;
                } else {//内部的驳回
                    return true;
                }
            }
            //技术部经理同意股份项目
            if (GroupUtils.equalsJs(groups, "jsb_jl") && value.equals("true") && project2.getDepAuditOpinion().equals("股份项目")) {
                if (comment == null)
                    comment = "";
                //添加评论
                Authentication.setAuthenticatedUserId(userId);
                taskService.addComment(task.getId(), pid, comment);
                //设置任务代理人
                taskService.setAssignee(task.getId(), userId);
                //设置经理同意参数
                taskService.setVariable(task.getId(), varName, value);
                //完成任务
                taskService.complete(task.getId());

                task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                //直接设置两会同意参数，去总经会
                taskService.setVariable(task.getId(), "lh", 0);
                //完成任务
                taskService.complete(task.getId());
                return true;
            }

            //立项部门经理处理同意项目(peoples是技术部经办人)
            if (GroupUtils.equalsJs(groups, "jl") && value.equals("true")) {
                UserController userController = new UserController();
                //拿到该项目类型的所有技术部经办人
                List<UserOV> userOVS = userController.getAllJsbDoman(project2.getReviser(), null);
                String jbrs = userOVS.get(0).userId;
                for (int i = 1; i < userOVS.size(); i++) {
                    jbrs = jbrs + "," + userOVS.get(i).userId;
                }
                Fs fs = new Fs();
                fs.setProjectid(project2.getId());
                fs = fsMapper.selectOne(fs);
                //没有数据插入
                if (fs == null) {
                    fs = new Fs();
                    fs.setProjectid(project2.getId());
                    fs.setId(IdCreate.id());
                    fs.setJsbjbr(jbrs);
                    fsMapper.insert(fs);
                } else {//有数据就更新
                    fs.setJsbjbr(jbrs);
                    fsMapper.updateByPrimaryKeySelective(fs);
                }
            }

            if (GroupUtils.equalsJs(groups, "jsb_doman")) {//处理人为技术部办事人,设置经办人
                Project project = pidToProject(task.getProcessInstanceId());//
                //设置经办人
                project.setBider(identityService.createUserQuery().userId(userId).singleResult().getFirstName());
                //修改项目表
                projectMapper.updateByPrimaryKeySelective(project);
                project = projectMapper.selectByPrimaryKey(project.getId());
                System.out.println("修改后：" + project.getBider());
                // System.out.println(project.getId());
                Fs fs = new Fs();
                fs.setProjectid(project.getId());
                fs = fsMapper.selectOne(fs);
                if (fs == null) {
                    fs = new Fs();
                    fs.setId(IdCreate.id());
                    fs.setProjectid(project.getId());
                    fsMapper.insert(fs);
                }
                fs.setDojsbjbr(userId);
                //!isReject(project.getId())&&
                if (value.equals("true")) {//同意顺流项目
                    //修改发送表的技术部经办人和选择的技术部主管经理
                    String jsbzgjls = peoples[0];
                    for (int i = 1; i < peoples.length; i++) {
                        jsbzgjls = jsbzgjls + "," + peoples[i];
                    }
                    fs.setJsbzgjl(jsbzgjls);
                }
                fsMapper.updateByPrimaryKeySelective(fs);
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

                //如果是驳回的技术部的项目，驳回到填写申请表
                if (value.equals("false") && project.getDeclarationDep().equals("工程技术部")) {
                    //经理不同意经理不同意
                    task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                    //设置立项部门经理驳回参数
                    taskService.setVariable(task.getId(), "jl", false);
                    taskService.complete(task.getId());

                    //立项部门主管经理不同意
                    task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
                    //设置立项部门主管经理不同意参数
                    taskService.setVariable(task.getId(), "zgjl", false);
                    taskService.complete(task.getId());
                }
                return true;
            }

            //如果技术部主管经理处理，更新发送表，设置项目主管经理处理人
            if (GroupUtils.equalsJs(groups, "jsb_zgjl")) {
                Fs fs = new Fs();
                fs.setProjectid(project2.getId());
                fs = fsMapper.selectOne(fs);
                if (fs == null) {
                    fs = new Fs();
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
            //判断是否结束、如果结束记录时间
            if (varName.equals("zjl") && value.equals("2") || varName.equals("lh") && value.equals("1")) {
                Xmsjb xmsjb = new Xmsjb();
                xmsjb.setProjectid(projectMapper.pidToXmid(pid));
                xmsjb.setQqjssj(Time.getNow());
                xmsjbMapper.updateByPrimaryKeySelective(xmsjb);
            }
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }


    //备案
    @Transactional
    @RequestMapping("doba")
    public boolean doba(String pid, String userId, String comment, int value) {
        try {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            TaskService taskService = processEngine.getTaskService();
            Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
            if (comment == null)
                comment = "";
            //添加评论
            Authentication.setAuthenticatedUserId(userId);
            taskService.addComment(task.getId(), pid, comment);
            //设置任务代理人
            taskService.setAssignee(task.getId(), userId);
            //设置备案同意参数
            taskService.setVariable(task.getId(), "bm", value);
            //同意备案、设置前期结束时间
            if (value == 0) {
                Xmsjb xmsjb = new Xmsjb();
                xmsjb.setProjectid(projectMapper.pidToXmid(pid));
                xmsjb.setQqjssj(Time.getNow());
                xmsjbMapper.updateByPrimaryKeySelective(xmsjb);
            }
            //完成任务
            taskService.complete(task.getId());
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
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
        //设置前期结束时间
        Xmsjb xmsjb = new Xmsjb();
        xmsjb.setProjectid(projectMapper.pidToXmid(pid));
        xmsjb.setQqjssj(Time.getNow());
        xmsjbMapper.updateByPrimaryKeySelective(xmsjb);
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

    //技术部经理和办公室只拿经办过项目的数量
    @RequestMapping("/getXmnum")
    public int getXmnum(String userId) {
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
        return res.size();
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
        //填充当前节点
        for (Project project : res)
            addDqjd(project);
        return res;
    }

    public void addDqjd(Project project) {
        if (project.getPid() == null || project.getPid().equals(""))
            project.setDqjd("未申请");
        else
            project.setDqjd(getPidNode(project.getPid()));
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
        List<Project> projects = projectMapper.getPidProject();
        if (userId.equals("syc")) {//苏燕春拿股份备案
            for (Project project : projects) {
                if (project.getDepAuditOpinion().equals("股份项目") && isBa(project.getPid())) {
                    bas.add(project);
                }
            }
        } else if (userId.equals("db")||userId.equals("lze")) {
            for (Project project : projects) {
                if (isBa(project.getPid())  && !project.getDepAuditOpinion().equals("股份项目")) {
                    bas.add(project);
                }
            }
        } else {
            for (Project project : projects) {
                ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
                IdentityService identityService = engine.getIdentityService();
                User user = identityService.createUserQuery().userId(userId).singleResult();
                //如果该项目到达备案，且经办人为自己,且不是股份项目
                if (isBa(project.getPid()) && project.getBider().equals(user.getFirstName()) && !project.getDepAuditOpinion().equals("股份项目")) {
                    bas.add(project);
                }
            }
        }
        for (Project project : bas)
            addDqjd(project);
        return bas;
    }

    //流程id拿项目
    Project pidToProject(String pid) {
        Project project = new Project();
        project.setPid(pid);
        return projectMapper.selectOne(project);
    }

    //拿到前期管理的角色
    Group getQqjs(List<Group> groups) {
        List<String> qqjss = new ArrayList<>();
        qqjss.add("bgs");
        qqjss.add("doman");
        qqjss.add("jl");
        qqjss.add("jsb_doman");
        qqjss.add("jsb_jl");
        qqjss.add("jsb_zgjl");
        qqjss.add("zgjl");
        for (Group group : groups) {
            if (qqjss.contains(group.getId())) {
                return group;
            }
        }
        return null;
    }

    //领取项目(前期审批)
    @RequestMapping("/lqxm")
    public List<Project> lqxm(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        //查询所有职位
        List<Group> groups = identityService.createGroupQuery().groupMember(userId).list();
        //查询部门
        String did = identityService.getUserInfo(userId, "departmentId");
        //第一个职位的任务
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(groups.get(0).getId()).list();
        //查询组下面的任务
        for (int i = 1; i < groups.size(); i++) {
            tasks.addAll(taskService.createTaskQuery().taskCandidateGroup(groups.get(i).getId()).list());
        }
        //过滤掉不是前期的任务
        TaskUtil.filterOtherLc(tasks, "lxsp");
        //过滤，查询自己部门下的任务,找到后返回
        List<Project> projects = new ArrayList<>();
        if (did.equals("20190123022801622")) {//工程技术部
            for (Task task : tasks) {
                Project project = pidToProject(task.getProcessInstanceId());
                if (project != null)
                    projects.add(project);
            }
        } else if (did.equals("20190125102616787")) {//办公室拿两会
            for (Task task : tasks) {
                Project project = pidToProject(task.getProcessInstanceId()); //(Project) taskService.getVariable (task.getId(), "project");
                if (project != null && getPidNode(project.getPid()).equals("两会"))
                    projects.add(project);
            }
        } else {//其他部门就需要过滤
            for (Task task : tasks) {
                Project project = pidToProject(task.getProcessInstanceId());//(Project) taskService.getVariable(task.getId(), "project");
                if (project != null && project.getDeclarationDep().equals(dnam(did))) {
                    projects.add(project);
                }
            }
        }
        //如果是办事员，拿到项目，判定是不是自己的申请人，
        if (GroupUtils.equalsJs(groups, "doman")) {
            List<Project> res = new ArrayList<>();
            //用户名
            String userName = identityService.createUserQuery().userId(userId).singleResult().getFirstName();
            for (int i = 0; i < projects.size(); i++) {
                if (projects.get(i).getProposer().equals(userName)) {
                    res.add(projects.get(i));
                }
            }
            //如果用户是邓博，李泽恩拿到总经会，
            if (userId.equals("db") || userId.equals("lze")) {
                //查询办公室下面的任务
                List<Task> tasks2 = taskService.createTaskQuery().taskCandidateGroup("bgs").list();
                for (Task task : tasks2) {
                    Project project = pidToProject(task.getProcessInstanceId());//(Project) taskService.getVariable(task.getId(), "project");
                    if (project != null && project.getPid() != null && !project.getPid().equals("") && getPidNode(project.getPid()).equals("总经理办公会")) {
                        res.add(project);
                    }
                }
            }
            for (Project project : res)
                addDqjd(project);
            return res;
        }
        //技术部办事员领取项目
        if (GroupUtils.equalsJs(groups, "jsb_doman")) {
            List<Project> res = new ArrayList<>();
            for (Project project : projects) {
                Fs fs = new Fs();
                fs.setProjectid(project.getId());
                fs = fsMapper.selectOne(fs);
                //以前的没有数据，都发送
                if (fs == null) {
                    res.add(project);
                    continue;
                }
                if (fs.getJsbjbr() == null) {
                    res.add(project);
                    continue;
                }
                if (isReject(project.getId())) {//如果是驳回项目，判断发送表中的dojsbjbr是不是自己
                    if (fs.getDojsbjbr() == null || fs.getDojsbjbr().equals("") || fs.getDojsbjbr().equals(userId)) {
                        res.add(project);
                    }
                } else {//如果是顺流项目，判断发送表中的jsbjbr有没有自己
                    //不是工程技术部的顺流项目
                    if (!project.getDeclarationDep().equals("工程技术部")) {
                        String[] jbrs = fs.getJsbjbr().split(",");
                        for (int i = 0; i < jbrs.length; i++) {
                            if (jbrs[i].equals(userId)) {
                                res.add(project);
                                break;
                            }
                        }
                    } else {//工程技术部的顺流项目
                        //还没有工程技术部处理人
                        if (fs.getDojsbjbr() == null || fs.getDojsbjbr().equals("")) {
                            String[] jbrs = fs.getJsbjbr().split(",");
                            for (int i = 0; i < jbrs.length; i++) {
                                if (jbrs[i].equals(userId)) {
                                    res.add(project);
                                    break;
                                }
                            }
                        } else {//有工程技术部处理人
                            if (fs.getDojsbjbr().equals(userId)) {
                                res.add(project);
                                continue;
                            }
                        }
                    }
                }
            }
            //过滤掉备案项目
            for (int i = 0; i < res.size(); i++) {
                if (isBa(res.get(i).getPid())) {
                    res.remove(i);
                    i--;
                }
            }
            for (Project project : res)
                addDqjd(project);
            return res;
        }
        //技术部主管经理领取项目
        if (GroupUtils.equalsJs(groups, "jsb_zgjl")) {
            List<Project> res = new ArrayList<>();
            for (Project project : projects) {
                Fs fs = new Fs();
                fs.setProjectid(project.getId());
                fs = fsMapper.selectOne(fs);
                //以前的没有数据，都发送
                if (fs == null) {
                    res.add(project);
                    continue;
                }
                if (isReject(project.getId())) {//如果是驳回项目，判断发送表中的dojsbzgjl是不是自己
                    if (fs.getDojsbzgjl() == null || fs.getDojsbzgjl().equals("") || fs.getDojsbzgjl().equals(userId)) {
                        res.add(project);
                    }
                } else {//如果是顺流项目，判断发送表中的jsbzgjl有没有自己
                    if (fs.getJsbzgjl() == null || fs.getJsbzgjl().equals("")) {
                        res.add(project);
                        continue;
                    }
                    String[] jsbzgjls = fs.getJsbzgjl().split(",");
                    for (int i = 0; i < jsbzgjls.length; i++) {
                        if (jsbzgjls[i].equals(userId)) {
                            res.add(project);
                            break;
                        }
                    }
                }
            }
            for (Project project : res)
                addDqjd(project);
            return res;
        }
        for (Project project : projects)
            addDqjd(project);
        return projects;
    }

    //查看申请状态
//    @RequestMapping("/zt")
//    public String zt(String pi) throws IOException {
//        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
//        RuntimeService runtimeService = processEngine.getRuntimeService();
//        RepositoryService repositoryService = processEngine.getRepositoryService();
//        //根据pid找流程名字
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(pi).singleResult();
//        String pidName = processInstance.getProcessDefinitionKey();
//        //查询流程定义
//        ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(pidName).list().get(0);
//        //获取bpmn模型对象
//        BpmnModel model = repositoryService.getBpmnModel(pd.getId());
//        //定义使用宋体
//        String fontName = "AR PL UMing HK";
//        //获取流程实例当前的节点,需要高亮显示
//        List<String> currentActs = runtimeService.getActiveActivityIds(pi);
//        //BPMN模型对象,图片类型,显示的节点
//        InputStream is = processEngine.getProcessEngineConfiguration()
//                .getProcessDiagramGenerator()
//                .generateDiagram(model, "png", currentActs, new ArrayList<String>(), fontName, fontName, fontName, null, 1.0);
//
//        //将输入流转换为byte数组
//        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
//        int b;
//        while ((b = is.read()) != -1) {
//            bytestream.write(b);
//        }
//        byte[] bs = bytestream.toByteArray();
//        BASE64Encoder encoder = new BASE64Encoder();
//        String data = encoder.encode(bs);
//        return data;
//    }

    //上传附件
    @Transactional
    @RequestMapping(value = "/uploadFile")
    public boolean uploadFile(MultipartFile file, String pId, String userId, String bcwjid) {
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
            Yscjdwj yscjdwj = new Yscjdwj();
            yscjdwj.setId(IdCreate.id());
            yscjdwj.setJlid(pidToProject(pId).getId());
            yscjdwj.setBcwjid(bcwjid);
            yscjdwj.setFid(attachment.getId());
            yscjdwj.setFname(file.getOriginalFilename());
            yscjdwj.setScr(userId);
            yscjdwj.setY1(Time.getNow());
            yscjdwjMapper.insert(yscjdwj);
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
            if (comments.get(0).getFullMessage().length() >= 3) {
                if (comments.get(0).getFullMessage().substring(0, 3).equals("驳回：")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    //项目id拿评论
    @RequestMapping("projectIdTocomment")
    public List<Return_Comments> projectIdTocomment(String projectId) {
        Project project = projectMapper.selectByPrimaryKey(projectId);
        if (project.getPid() == null || project.getPid().equals(""))
            return null;
        return projecttocomment(project.getPid());
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
            List<Group> groups = identityService.createGroupQuery().groupMember(uid).list();
            String unam = user.getFirstName();
            Return_Comments return_comments1 = new Return_Comments();
            return_comments1.setComment(comment.getFullMessage());
            return_comments1.setUsernam(unam);
            Group group = getQqjs(groups);
            return_comments1.setGroupName(group.getName());
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
    public List<String> startpapplication(@RequestBody Project pa) {
        try {
            List<String> res = new ArrayList<>();
            String id = IdCreate.id();
            pa.setId(id);
            //去掉项目名首尾空格
            pa.setProjectNam(pa.getProjectNam().trim());
            String createTime = Time.getNow();
            pa.setEngTechAuditOpinion(createTime);
            projectMapper.insert(pa);
            //插入项目时间表
            Xmsjb xmsjb = new Xmsjb();
            xmsjb.setProjectid(id);
            xmsjb.setCjsj(createTime);
            xmsjbMapper.insert(xmsjb);
            res.add(id);
            res.add(pa.getEngTechAuditOpinion());
            return res;
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
        //判断项目是否工程技术部
        if (!project.getDeclarationDep().equals("工程技术部")) {
            if (project.getPid() != null && !project.getPid().equals("") && !getPidNode(project.getPid()).equals("填写申请表"))
                return false;
        } else {
            //如果是工程技术部项目，在经办人就可以删除
            if (project.getPid() != null && !project.getPid().equals("") && !(getPidNode(project.getPid()).equals("经办人") || getPidNode(project.getPid()).equals("填写申请表")))
                return false;
        }
        return true;
    }

    //删除
    @Transactional
    @RequestMapping("/deletXm")
    public boolean deletXm(String xmid) {
        if (canDelet(xmid)) {
            Project po = projectMapper.selectByPrimaryKey(xmid);
            projectMapper.deleteByPrimaryKey(xmid);
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

    //确定修改按钮的显示
    @RequestMapping("/qdsqr")
    public boolean qdsqr(String projectId, String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        String username = user.getFirstName();
        Project po = projectMapper.selectByPrimaryKey(projectId);
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

    //总经会时间的添加
    @Transactional
    @RequestMapping("/xgzjhsj")
    public boolean xgzjhsj(String xmid, String zjhsj) {
        try {
            Project project = projectMapper.selectByPrimaryKey(xmid);
            project.setZjhsj(zjhsj);
            projectMapper.updateByPrimaryKeySelective(project);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //办公室修改项目表单的项目号和两会时间
    @Transactional
    @RequestMapping("/xgxmbhAndlhsj")
    public boolean xgxmbh(String xmid, String xmbh, String lhsj) {
        try {
            Project project = projectMapper.selectByPrimaryKey(xmid);
            project.setProjectNo(xmbh);
            project.setLhsj(lhsj);
            //修改项目表
            projectMapper.updateByPrimaryKeySelective(project);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //修改项目表单
    @Transactional
    @RequestMapping("/updataXm")
    public boolean xgxmbd(@RequestBody Project po) {
        return projectMapper.updateByPrimaryKeySelective(po) == 1;
    }

    //拿到本部门未开始招标的项目id和项目name
    //拿到是自己经办人的能招标的项目
    @RequestMapping("/getSelfWzzXmidAndXmname")
    public List<Xm> getSelfWzzXmidAndXmname(String userName) {
        List<Map> maps = projectMapper.selectJsbjbrXmidAndXmname(userName);
        //自己的所有项目
        List<Xm> xms = new ArrayList<>();
        for (int i = 0; i < maps.size(); i++) {
            Xm xm = new Xm();
            xm.label = maps.get(i).get("PROJECT_NAM").toString();
            xm.value = maps.get(i).get("ID").toString();
            xms.add(xm);
        }
        //所有招标了的项目id
        List<String> projectIds = new ArrayList<>();
        for (Zhaobiao zhaobiao : zhaobiaoMapper.selectAll()) {
            projectIds.add(zhaobiao.getXmid());
        }
        List<Xm> res = new ArrayList<>();
        for (Xm xm : xms) {
            Project project = projectMapper.selectByPrimaryKey(xm.value);
            if (!projectIds.contains(xm.value) && !project.getDepAuditOpinion().equals("股份项目")) {
                res.add(xm);
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
    public List<Xm> getCanHtXmIdAndXmname(String userName) {
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
        //在小项目管理中找
        List<String> xmids = new ArrayList<>();
        List<Xxmgl> xxmgls = xxmglMapper.selectAll();
        for (Xxmgl xxmgl : xxmgls) {
            xmids.add(xxmgl.getY1());
        }
        List<String> xmids2 = new ArrayList<>();
        for (Xm xm : xms) {
            xmids2.add(xm.value);
        }
        for (int i = 0; i < xmids.size(); i++) {
            if (!xmids2.contains(xmids.get(i))) {
                Xm xm = new Xm();
                xm.value = xmids.get(i);
                xm.label = projectMapper.selectByPrimaryKey(xmids.get(i)).getProjectNam();
                xms.add(xm);
            }
        }
        List<String> contracts = contractMapper.selectAllXmids();
        for (int i = 0; i < xms.size(); i++) {
            if (contracts.contains(xms.get(i).value)) {
                xms.remove(i);
                i--;
            }
        }
        //拿自己的项目的合同
        for (int i = 0; i < xms.size(); i++) {
            String jbr = projectMapper.selectByPrimaryKey(xms.get(i).value).getBider();
            if (!jbr.equals(userName)) {
                xms.remove(i);
                i--;
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

    //项目id拿项目详情
    @RequestMapping("/getXmById")
    public Project getXmById(String xmid) {
        return projectMapper.selectByPrimaryKey(xmid);
    }

    //修改推荐单位
    @RequestMapping("/updataTjdw")
    public void updataTjdw(String xmid, String tjdw) {
        Project project = projectMapper.selectByPrimaryKey(xmid);
        project.setTjdw(tjdw);
        projectMapper.updateByPrimaryKeySelective(project);
    }

    //拿项目名称和编号
    @RequestMapping("/selectNameAndNo")
    public Map selectNameAndNo(String xmid) {
        return projectMapper.selectNameAndNo(xmid);
    }

    //图形化统计
    @RequestMapping("/txhtj")
    public List<Map> selectXmidAndXmnoAndXmname() {
        return projectMapper.selectXmidAndXmnoAndXmname();
    }

    //判断立项审批流程是第几版本、
    @RequestMapping("getLxspBb")
    int getLxspBb(String pid) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        TaskService taskService = processEngine.getTaskService();
        Task task = taskService.createTaskQuery().processInstanceId(pid).singleResult();
        return Integer.valueOf(task.getProcessDefinitionId().split(":")[1]);
    }
}
