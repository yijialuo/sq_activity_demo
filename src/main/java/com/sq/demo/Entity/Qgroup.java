package com.sq.demo.Entity;

import org.activiti.engine.identity.Group;

/**
 * Created by yijialuo on 2019/1/16.
 * 前端需要的group
 */
public class Qgroup {
    String id;
    String name;
    String code;

    //把activity中的group转换为前端需要的group
    void GroupToQgroup(Group group){
        this.id=group.getId();
        this.name=group.getName();
        this.code=group.getType();
    }
}
