package com.sq.demo.pojo;

import javax.persistence.*;

public class Bcwj {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 流程
     */
    private String lc;

    /**
     * 节点
     */
    private String jd;

    /**
     * 文件名称
     */
    private String wjmc;

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
     * 获取流程
     *
     * @return lc - 流程
     */
    public String getLc() {
        return lc;
    }

    /**
     * 设置流程
     *
     * @param lc 流程
     */
    public void setLc(String lc) {
        this.lc = lc;
    }

    /**
     * 获取节点
     *
     * @return jd - 节点
     */
    public String getJd() {
        return jd;
    }

    /**
     * 设置节点
     *
     * @param jd 节点
     */
    public void setJd(String jd) {
        this.jd = jd;
    }

    /**
     * 获取文件名称
     *
     * @return wjmc - 文件名称
     */
    public String getWjmc() {
        return wjmc;
    }

    /**
     * 设置文件名称
     *
     * @param wjmc 文件名称
     */
    public void setWjmc(String wjmc) {
        this.wjmc = wjmc;
    }
}