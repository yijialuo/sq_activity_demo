package com.sq.demo.pojo;

import javax.persistence.*;

public class Score {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 总评价id
     */
    @Column(name = "SID")
    private String sid;

    /**
     * 评分年份
     */
    @Column(name = "PFNF")
    private String pfnf;

    /**
     * 考核内容
     */
    @Column(name = "KHNR")
    private String khnr;

    /**
     * 考核部门
     */
    @Column(name = "KHBM")
    private String khbm;

    /**
     * 评分
     */
    @Column(name = "PF")
    private String pf;

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
     * 获取总评价id
     *
     * @return SID - 总评价id
     */
    public String getSid() {
        return sid;
    }

    /**
     * 设置总评价id
     *
     * @param sid 总评价id
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * 获取评分年份
     *
     * @return PFNF - 评分年份
     */
    public String getPfnf() {
        return pfnf;
    }

    /**
     * 设置评分年份
     *
     * @param pfnf 评分年份
     */
    public void setPfnf(String pfnf) {
        this.pfnf = pfnf;
    }

    /**
     * 获取考核内容
     *
     * @return KHNR - 考核内容
     */
    public String getKhnr() {
        return khnr;
    }

    /**
     * 设置考核内容
     *
     * @param khnr 考核内容
     */
    public void setKhnr(String khnr) {
        this.khnr = khnr;
    }

    /**
     * 获取考核部门
     *
     * @return KHBM - 考核部门
     */
    public String getKhbm() {
        return khbm;
    }

    /**
     * 设置考核部门
     *
     * @param khbm 考核部门
     */
    public void setKhbm(String khbm) {
        this.khbm = khbm;
    }

    /**
     * 获取评分
     *
     * @return PF - 评分
     */
    public String getPf() {
        return pf;
    }

    /**
     * 设置评分
     *
     * @param pf 评分
     */
    public void setPf(String pf) {
        this.pf = pf;
    }
}