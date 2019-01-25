package com.sq.demo.pojo;

import javax.persistence.*;

public class File {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 路径
     */
    @Column(name = "URL")
    private String url;

    /**
     * 投标商
     */
    @Column(name = "BIDER")
    private String bider;

    /**
     * 项目id
     */
    @Column(name = "PROJECT_ID")
    private String projectId;

    /**
     * 上传部门
     */
    @Column(name = "DEPARTMENT_ID")
    private String departmentId;

    /**
     * 获取主键
     *
     * @return ID - 主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取路径
     *
     * @return URL - 路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置路径
     *
     * @param url 路径
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取投标商
     *
     * @return BIDER - 投标商
     */
    public String getBider() {
        return bider;
    }

    /**
     * 设置投标商
     *
     * @param bider 投标商
     */
    public void setBider(String bider) {
        this.bider = bider;
    }

    /**
     * 获取项目id
     *
     * @return PROJECT_ID - 项目id
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * 设置项目id
     *
     * @param projectId 项目id
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取上传部门
     *
     * @return DEPARTMENT_ID - 上传部门
     */
    public String getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置上传部门
     *
     * @param departmentId 上传部门
     */
    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }
}