package com.sq.demo.controller;

import com.sq.demo.mapper.DepartmentMapper;
import com.sq.demo.pojo.Department;
import com.sq.demo.utils.IdCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * Created by yijialuo on 2019/1/15.
 */
@RestController
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    DepartmentMapper departmentMapper;

    //查询所有部门
    @RequestMapping("/getAllDepartment")
    List<Department> getDepartment() {
        try {
            return departmentMapper.selectAll();
        } catch (Exception e) {
            return null;
        }

    }

    //保存department
    @RequestMapping("/insertDepartment")
    Department insertDepartment(@RequestBody Department department) {
        System.out.println(department.getdNam());
        try {
            department.setId(IdCreate.id());
            departmentMapper.insert(department);
            return department;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    //修改department
    @RequestMapping("/updataDepartment")
    boolean updataDepartment(@RequestBody Department department) {
        try {
            departmentMapper.updateByPrimaryKey(department);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}