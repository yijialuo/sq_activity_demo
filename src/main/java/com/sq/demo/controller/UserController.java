package com.sq.demo.controller;

import com.sq.demo.Entity.Return_Comments;
import com.sq.demo.Entity.UserOV;
import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.mapper.FsMapper;
import com.sq.demo.mapper.ProjectMapper;
import com.sq.demo.mapper.QxMapper;
import com.sq.demo.pojo.Department;
import com.sq.demo.pojo.Fs;
import com.sq.demo.pojo.Project;
import com.sq.demo.utils.ArrayToString;
import com.sq.demo.utils.GroupUtils;
import com.sq.demo.utils.IdCreate;
import com.sq.demo.utils.RedisUtil;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
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
    @Autowired
    ProjectController projectController;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    QxMapper qxMapper;

    //判断是不是技术部的人
    @RequestMapping("/isJsb")
    public boolean isJsb(String userId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        return identityService.getUserInfo(userId, "departmentId").equals("20190123022801622");
    }

    //拿主管经理
    @RequestMapping("/getAllJsbZgjl")
    public List<UserOV> getAllJsbZgjl(String manageType, String projectId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<UserOV> userOVS = new ArrayList<>();
        if (projectId == null || projectId.equals("")) {
            //拿所有的用户
            List<User> users = identityService.createUserQuery().list();
            for (User user : users) {
                List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
                if (GroupUtils.equalsJs(groups, "jsb_zgjl") && identityService.getUserInfo(user.getId(), "manageType").contains(manageType)) {
                    UserOV userOV = new UserOV();
                    userOV.userId = user.getId();
                    userOV.userName = user.getFirstName();
                    userOVS.add(userOV);
                }
            }
            return userOVS;
        } else {//拿到处理的技术部主管经理
            Fs fs = new Fs();
            fs.setProjectid(projectId);
            fs = fsMapper.selectOne(fs);
            if (fs == null || fs.getDojsbzgjl() == null || fs.getDojsbzgjl().equals("")) {
                Project project = projectMapper.selectByPrimaryKey(projectId);
                return getAllJsbZgjl(project.getReviser(), null);
            }
            UserOV userOV = new UserOV();
            userOV.userId = fs.getDojsbzgjl();
            userOV.userName = identityService.createUserQuery().userId(fs.getDojsbzgjl()).singleResult().getFirstName();
            userOVS.add(userOV);
            return userOVS;
        }
    }


    //拿发起人
    @RequestMapping("/fqr")
    public List<String> fqr() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<User> users = identityService.createUserQuery().list();
        List<String> res = new ArrayList<>();
        for (User user : users) {
            List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
            if (GroupUtils.equalsJs(groups, "jsb_doman") || GroupUtils.equalsJs(groups, "doman")) {
                res.add(user.getFirstName());
            }
        }
        return res;
    }

    //拿技术部经办人
    @RequestMapping("/jsbjbr")
    public List<String> jsbjbr() {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<User> users = identityService.createUserQuery().list();
        List<String> res = new ArrayList<>();
        for (User user : users) {
            List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
            if (GroupUtils.equalsJs(groups, "jsb_doman")) {
                res.add(user.getFirstName());
            }
        }
        return res;
    }

    //拿技术部经办人
    @RequestMapping("/getAllJsbDoman")
    public List<UserOV> getAllJsbDoman(String manageType, String projectId) {
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        List<UserOV> userOVS = new ArrayList<>();
        if (projectId == null || projectId.equals("")) {//拿所有
            List<User> users = identityService.createUserQuery().list();
            for (User user : users) {
                List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
                if (GroupUtils.equalsJs(groups, "jsb_doman") && Arrays.asList(identityService.getUserInfo(user.getId(),"manageType").split(",")).contains(manageType)) {
                    UserOV userOV = new UserOV();
                    userOV.userId = user.getId();
                    userOV.userName = user.getFirstName();
                    userOVS.add(userOV);
                }
            }
            return userOVS;
        } else {//拿处理的经办人
            Fs fs = new Fs();
            fs.setProjectid(projectId);
            fs = fsMapper.selectOne(fs);
            if (fs == null) {
                Project project = projectMapper.selectByPrimaryKey(projectId);
                return getAllJsbDoman(project.getReviser(), null);
            }
            UserOV userOV = new UserOV();
            userOV.userId = fs.getDojsbjbr();
            userOV.userName = identityService.createUserQuery().userId(fs.getDojsbjbr()).singleResult().getFirstName();
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
            if (!departmentName.equals("工程技术部")&&!departmentName.equals("办公室")) {
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
        } catch (Exception e) {
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
            if (!departmentId.equals("20190123022801622")&&!departmentId.equals("20190125102616787")) {
                for (User user : users) {
                    //找到部门的人
                    if (identityService.getUserInfo(user.getId(), "departmentId").equals(departmentId)) {
                        List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
                        //找到doman
                        if (GroupUtils.equalsJs(groups, "doman")) {
                            UserOV userOV = new UserOV();
                            userOV.userId = user.getId();
                            userOV.userName = user.getFirstName();
                            res.add(userOV);
                        }
                    }
                }
            } else {//如果是工程技术部拿所有的
                for (User user : users) {
                    List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
                    if (GroupUtils.equalsJs(groups, "doman") || GroupUtils.equalsJs(groups, "jsb_doman")) {
                        UserOV userOV = new UserOV();
                        userOV.userId = user.getId();
                        userOV.userName = user.getFirstName();
                        res.add(userOV);
                    }
                }
            }
            return res;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/login")
    public boolean dologin(HttpServletResponse res, HttpServletRequest req, String username, String password) throws UnsupportedEncodingException {
        String clientIp = getIPAddress(req);
        //ip连续登录错误5次，禁止登录，
        if(redisUtil.get(clientIp)!=null&&redisUtil.get(clientIp).equals(5)){
            res.setHeader("info", URLEncoder.encode("密码连续输错5次！请一分钟后再试！", "UTF-8"));
            return false;
        }
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        if (identityService.checkPassword(username, password)) {
            String token = username + "=" + IdCreate.id();
            res.setHeader("authorization", token);
            //存redis token
            redisUtil.set(username, token, 172800);
            //登录成功、清空登录错误次数
            redisUtil.del(clientIp);
            return true;
        } else {//登录失败
            //redis增加ip登录次数
            long loginerrorCount=redisUtil.incr(clientIp, 1);
            //登录错误次数达到3次，设置1分钟时间
            if(loginerrorCount==5){
                redisUtil.expire(clientIp,60);
            }
            res.setHeader("info",URLEncoder.encode("密码错误！","UTF-8"));
            return false;
        }
    }

    //客户端ip
    public static String getIPAddress(HttpServletRequest request) {
        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }
        return ip;
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
            //userOV.qxs=qxMapper.getZjids(userId);
            //查询用户所在的组
            List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
            userOV.groupId = GroupUtils.getGroupIds(groups);
             if (userOV.departmentId != null && !userOV.departmentId.equals("")) {
                Department department = new Department();
                department.setId(userOV.departmentId);
                String d_name = departmentMapper.selectOne(department).getdNam();
                userOV.departmentName = d_name;
            }
            userOV.groupName = GroupUtils.getGroupNames(groups);
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
        if (userOV.departmentId != null && !userOV.departmentId.equals(""))
            identityService.setUserInfo(user.getId(), "departmentId", userOV.departmentId);
        //选择了manageType
        if (userOV.manageType != null && userOV.manageType.length != 0) {
            identityService.setUserInfo(user.getId(), "manageType", ArrayToString.array(userOV.manageType));
        }

        identityService.saveUser(user);
        for (String groupId : userOV.groupId) {
            identityService.createMembership(user.getId(), groupId);
        }
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
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        if (identityService.checkPassword(userId, oldPass)) {
            User user = identityService.createUserQuery().userId(userId).singleResult();
            user.setPassword(newPass);
            identityService.saveUser(user);
            return true;
        }
        return false;
    }

    public boolean equalList(List list1, List list2) {
        return (list1.size() == list2.size()) && list1.containsAll(list2);
    }

    @Transactional
    @RequestMapping("/edituser")
    public boolean edituser(@RequestBody UserOV userOV) {
        ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = processEngine.getIdentityService();
        User user = identityService.createUserQuery().userId(userOV.userId).singleResult();
        user.setFirstName(userOV.userName);
        user.setPassword(userOV.passWord);
        //原来的组
        List<Group> group = identityService.createGroupQuery().groupMember(userOV.userId).list();
        List<String> groupId = new ArrayList<>();
        for (Group group1 : group) {
            groupId.add(group1.getId());
        }
        //新的groupId不等于原来的就修改
        if (!equalList(userOV.groupId, groupId)) {
            //删除原来的组
            for (String groupId2 : groupId) {
                identityService.deleteMembership(userOV.userId, groupId2);
            }
            //增加现在的组
            for (String groupId3 : userOV.groupId) {
                identityService.createMembership(user.getId(), groupId3);
            }
        }
        //修改部门ID
        identityService.setUserInfo(user.getId(), "departmentId", userOV.departmentId);
        //修改处理类型
        if (userOV.manageType != null && userOV.manageType.length != 0) {
            identityService.setUserInfo(user.getId(), "manageType", ArrayToString.array(userOV.manageType));

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
                        if (identityService.getUserInfo(user.getId(), "manageType") != null) {
                            userOV.manageType = identityService.getUserInfo(user.getId(), "manageType").split(",");
                        }
                        //查询用户所在的组
                        List<Group> groups = identityService.createGroupQuery().groupMember(user.getId()).list();
                        userOV.groupId = GroupUtils.getGroupIds(groups);
                        Department department = new Department();
                        department.setId(userOV.departmentId);
                        userOV.departmentName = departmentMapper.selectOne(department).getdNam();
                        userOV.groupName = GroupUtils.getGroupNames(groups);
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

    //判断用户是否为该项目前期的处理人之一
    @RequestMapping("/isClr")
    public boolean isClr(String xmid, String userId) {
        Project project = projectMapper.selectByPrimaryKey(xmid);
        ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
        IdentityService identityService = engine.getIdentityService();
        User user = identityService.createUserQuery().userId(userId).singleResult();
        if (project.getProposer().equals(user.getFirstName())||project.getBider()!=null&&project.getBider().equals(user.getFirstName())) {
            return true;
        }
        if (project.getPid() != null && !project.getPid().equals("")) {
            for (Return_Comments return_comments : projectController.projecttocomment(project.getPid())) {
                if (return_comments.getUsernam().equals(user.getFirstName())) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }
}
