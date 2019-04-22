package com.sq.demo.controller;

import com.sq.demo.Entity.UserOV;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.FsMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.pojo.Department;
import com.sq.demo.pojo.Fs;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.ArrayToString;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    FsMapper fsMapper;


    //拿主管经理
    @RequestMapping("/getAllJsbZgjl")
    public List<UserOV> getAllJsbZgjl(String manageType,String projectId){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        List<UserOV> userOVS=new ArrayList<>();
        if(projectId==null||projectId.equals("")){
            List<User> users=identityService.createUserQuery().list();
            for(User user:users){
                Group group=identityService.createGroupQuery().groupMember(user.getId()).singleResult();
                if(group.getId().equals("jsb_zgjl")&&identityService.getUserInfo(user.getId(),"manageType").contains(manageType)){
                    UserOV userOV=new UserOV();
                    userOV.userId=user.getId();
                    userOV.userName=user.getFirstName();
                    userOVS.add(userOV);
                }
            }
            return userOVS;
        }else {//拿到处理的技术部主管经理
            Fs fs=new Fs();
            fs.setProjectid(projectId);
            fs=fsMapper.selectOne(fs);
            if(fs==null){
                Project project=projectMapper.selectByPrimaryKey(projectId);
                return getAllJsbZgjl(project.getReviser(),null);
            }
            UserOV userOV=new UserOV();
            userOV.userId=fs.getDojsbzgjl();
            userOV.userName=identityService.createUserQuery().userId(fs.getDojsbzgjl()).singleResult().getFirstName();
            userOVS.add(userOV);
            return userOVS;
        }
    }

    //拿技术部经办人
    @RequestMapping("/getAllJsbDoman")
    public List<UserOV> getAllJsbDoman(String manageType,String projectId){
        ProcessEngine engine=ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService=engine.getIdentityService();
        List<UserOV> userOVS=new ArrayList<>();
        if(projectId==null||projectId.equals("")){//拿所有
            List<User> users=identityService.createUserQuery().list();
            for(User user:users){
                Group group=identityService.createGroupQuery().groupMember(user.getId()).singleResult();
                if(group.getId().equals("jsb_doman")&&identityService.getUserInfo(user.getId(),"manageType").contains(manageType)){
                    UserOV userOV=new UserOV();
                    userOV.userId=user.getId();
                    userOV.userName=user.getFirstName();
                    userOVS.add(userOV);
                }
            }
            return userOVS;
        }else {//拿处理的经办人
            Fs fs=new Fs();
            fs.setProjectid(projectId);
            fs=fsMapper.selectOne(fs);
            if(fs==null){
                Project project=projectMapper.selectByPrimaryKey(projectId);
                return getAllJsbDoman(project.getReviser(),null);
            }
            UserOV userOV=new UserOV();
            userOV.userId=fs.getDojsbjbr();
            userOV.userName=identityService.createUserQuery().userId(fs.getDojsbjbr()).singleResult().getFirstName();
            userOVS.add(userOV);
            return userOVS;
        }
    }

    //根据用户名字查询用户id
    public String getUserIdByUserName(String userName) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        User user = identityService.createUserQuery().userFirstName(userName).singleResult();
        return user.getId();
    }

    //拿到项目的经办人
    @RequestMapping("/getDepartmentjbr")
    public List<UserOV> getSelfDepartmentjbr(String departmentName) {
        try {
            List<Project> projects = projectMapper.selectAll();
            List<UserOV> userOVS = new ArrayList<>();
            if (!departmentName.equals("工程技术部")) {
                List<String> userNames = new ArrayList<>();
                for (Project project : projects) {
                    if (project.getDeclarationDep().equals(departmentName) && project.getBider() != null && !project.getBider().equals("")) {
                        UserOV userOV = new UserOV();
                        userOV.userName = project.getBider();
                        if (!userNames.contains(userOV.userName)) {//过滤重复
                            userOV.userId = getUserIdByUserName(userOV.userName);
                            userOVS.add(userOV);
                            userNames.add(userOV.userName);
                        }
                    }
                }
                return userOVS;
            } else {//是工程技术部
                List<String> userNames = new ArrayList<>();
                for (Project project : projects) {
                    if (project.getBider() != null && !project.getBider().equals("")) {
                        UserOV userOV = new UserOV();
                        userOV.userName = project.getBider();
                        if (!userNames.contains(userOV.userName)) {//过滤重复
                            userOV.userId = getUserIdByUserName(userOV.userName);
                            userOVS.add(userOV);
                            userNames.add(userOV.userName);
                        }
                    }
                }
                return userOVS;
            }
        }catch (Exception e){
            return null;
        }
    }

    //拿到部门的所有doman
    @RequestMapping("/getDepartmentDoman")
    public List<UserOV> getDeparmentDoman(String departmentId) {
        try {
            ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
            IdentityService identityService = engine.getIdentityService();
            List<User> users = identityService.createUserQuery().list();
            List<UserOV> res = new ArrayList<>();
            //如果不是工程技术部，只能拿自己部门的办事员
            if (!departmentId.equals("20190123022801622")) {
                for (User user : users) {
                    //找到部门的人
                    if (identityService.getUserInfo(user.getId(), "departmentId").equals(departmentId)) {
                        //找到doman
                        if (identityService.createGroupQuery().groupMember(user.getId()).singleResult().getId().equals("doman")) {
                            UserOV userOV = new UserOV();
                            userOV.userId = user.getId();
                            userOV.userName = user.getFirstName();
                            res.add(userOV);
                        }
                    }
                }
            } else {//如果是工程技术部拿所有的
                for (User user : users) {
                    String grouoId = identityService.createGroupQuery().groupMember(user.getId()).singleResult().getId();
                    if (grouoId.equals("doman") || grouoId.equals("jsb_doman")) {
                        UserOV userOV = new UserOV();
                        userOV.userId = user.getId();
                        userOV.userName = user.getFirstName();
                        res.add(userOV);
                    }
                }
            }
            return res;
        }catch (Exception e){
            return null;
        }
    }

    @RequestMapping("/login")
    public boolean dologin(String username, String password) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        return identityService.checkPassword(username, password);
    }

    @RequestMapping("/getuser")
    public UserOV getuser(String userId, String passWord) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        boolean check = identityService.checkPassword(userId, passWord);
        if (check) {
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
        } else {
            return null;
        }

    }

    @Transactional
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
        //选择了manageType
        if(userOV.manageType!=null&&userOV.manageType.length!=0){
            identityService.setUserInfo(user.getId(),"manageType", ArrayToString.array(userOV.manageType));
        }

        identityService.saveUser(user);
        identityService.createMembership(user.getId(), userOV.groupId);
        return true;
    }

    @Transactional
    @RequestMapping("/deleteuser")
    public boolean deleteuser(String userId) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        identityService.deleteUser(userId);
        return true;
    }


    //修改密码
    @Transactional
    @RequestMapping("/xgmm")
    public boolean xgmm(String userId, String oldPass, String newPass) {
        if (dologin(userId, oldPass)) {
            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
            IdentityService identityService = processEngine.getIdentityService();
            User user = identityService.createUserQuery().userId(userId).singleResult();
            user.setPassword(newPass);
            identityService.saveUser(user);
            return true;
        }
        return false;
    }

    @Transactional
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
        //修改处理类型
        if(userOV.manageType!=null&&userOV.manageType.length!=0){
            identityService.setUserInfo(user.getId(),"manageType",ArrayToString.array(userOV.manageType));

        }
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
        for (Group group : checkgroup) {
            if (group.getId().equals("admin")) {
                boolean check = identityService.checkPassword(userId, passWord);
                if (check) {
                    for (User user : users) {
                        UserOV userOV = new UserOV();
                        userOV.userId = user.getId();
                        userOV.userName = user.getFirstName();
                        userOV.passWord = user.getPassword();
                        userOV.departmentId = identityService.getUserInfo(user.getId(), "departmentId");
                        if(identityService.getUserInfo(user.getId(),"manageType")!=null){
                            userOV.manageType=identityService.getUserInfo(user.getId(),"manageType").split(",");
                        }
                        //查询用户所在的组
                        String groupId = identityService.createGroupQuery().groupMember(user.getId()).singleResult().getId();
                        userOV.groupId = groupId;
                        Department department = new Department();
                        department.setId(userOV.departmentId);
                        userOV.departmentName = departmentMapper.selectOne(department).getdNam();
                        Group group1 = identityService.createGroupQuery().groupId(groupId).singleResult();
                        userOV.groupName = group1.getName();
                        userOVs.add(userOV);
                    }
                    return userOVs;
                } else {
                    return null;
                }
            } else {
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
        for (Group group : checkgroup) {
            if (group.getId().equals("admin")) {
                return true;
            }
        }
        return false;
    }

    //userId To userName
    @RequestMapping("userIdTouserName")
    public String userIdTouserName(String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        String username = user.getFirstName();
        return username;
    }

    //根据uerId拿部门名称
    @RequestMapping("userIdToDept")
    public String userIdToDept(String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        String departmentId = identityService.getUserInfo(userId, "departmentId");
        return departmentMapper.selectByPrimaryKey(departmentId).getdNam();
    }
}
