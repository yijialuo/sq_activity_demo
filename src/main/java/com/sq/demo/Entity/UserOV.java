package com.sq.demo.Entity;


import java.util.List;

/**
 * Created by yijialuo on 2019/1/12.
 */
public class UserOV {
   public String userId;
   public String userName;
   public String passWord;
   public List<String> groupId;//职位
   public List<String> groupName;
   public String departmentId;//部门
   public String departmentName;
   public String[] manageType;
   //主键的权限
   //public List<String> qxs;
}
