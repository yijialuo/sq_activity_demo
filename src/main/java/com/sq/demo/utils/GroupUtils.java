package com.sq.demo.utils;

import org.activiti.engine.identity.Group;

import java.util.ArrayList;
import java.util.List;

public class GroupUtils {
    public static List<String> getGroupIds(List<Group> groups) {
        List<String> groupId = new ArrayList<>();
        for (Group group : groups) {
            groupId.add(group.getId());
        }
        return groupId;
    }

    public static List<String> getGroupNames(List<Group> groups) {
        List<String> groupName = new ArrayList<>();
        for (Group group : groups) {
            groupName.add(group.getName());
        }
        return groupName;
    }

    //判断是否有这个角色
    public static boolean equalsJs(List<Group> groups,String groupId){
        for(Group group:groups){
            if(group.getId().equals(groupId)){
                return true;
            }
        }
        return false;
    }
}
