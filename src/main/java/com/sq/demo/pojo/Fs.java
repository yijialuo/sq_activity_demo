package com.sq.demo.pojo;

import javax.persistence.*;

public class Fs {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 项目id
     */
    private String projectid;

    /**
     * 技术部经办人
     */
    private String jsbjbr;

    /**
     * 技术部主管经理
     */
    private String jsbzgjl;

    /**
     * 处理的技术部经办人
     */
    private String dojsbjbr;

    /**
     * 处理的技术部主管经理
     */
    private String dojsbzgjl;

    /**
     * 获取id
     *
     * @return id - id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取项目id
     *
     * @return projectid - 项目id
     */
    public String getProjectid() {
        return projectid;
    }

    /**
     * 设置项目id
     *
     * @param projectid 项目id
     */
    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    /**
     * 获取技术部经办人
     *
     * @return jsbjbr - 技术部经办人
     */
    public String getJsbjbr() {
        return jsbjbr;
    }

    /**
     * 设置技术部经办人
     *
     * @param jsbjbr 技术部经办人
     */
    public void setJsbjbr(String jsbjbr) {
        this.jsbjbr = jsbjbr;
    }

    /**
     * 获取技术部主管经理
     *
     * @return jsbzgjl - 技术部主管经理
     */
    public String getJsbzgjl() {
        return jsbzgjl;
    }

    /**
     * 设置技术部主管经理
     *
     * @param jsbzgjl 技术部主管经理
     */
    public void setJsbzgjl(String jsbzgjl) {
        this.jsbzgjl = jsbzgjl;
    }

    /**
     * 获取处理的技术部经办人
     *
     * @return dojsbjbr - 处理的技术部经办人
     */
    public String getDojsbjbr() {
        return dojsbjbr;
    }

    /**
     * 设置处理的技术部经办人
     *
     * @param dojsbjbr 处理的技术部经办人
     */
    public void setDojsbjbr(String dojsbjbr) {
        this.dojsbjbr = dojsbjbr;
    }

    /**
     * 获取处理的技术部主管经理
     *
     * @return dojsbzgjl - 处理的技术部主管经理
     */
    public String getDojsbzgjl() {
        return dojsbzgjl;
    }

    /**
     * 设置处理的技术部主管经理
     *
     * @param dojsbzgjl 处理的技术部主管经理
     */
    public void setDojsbzgjl(String dojsbzgjl) {
        this.dojsbzgjl = dojsbzgjl;
    }
}