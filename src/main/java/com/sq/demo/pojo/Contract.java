package com.sq.demo.pojo;

import java.math.BigDecimal;
import javax.persistence.*;

public class Contract {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 合同编号
     */
    @Column(name = "CONTRACT_NO")
    private String contractNo;

    /**
     * 合同项目
     */
    @Column(name = "PROJECT_ID")
    private String projectId;

    /**
     * 对方当事人
     */
    @Column(name = "DFDSR")
    private String dfdsr;

    /**
     * 投资文号
     */
    @Column(name = "TZWH")
    private String tzwh;

    /**
     * 合同价款
     */
    @Column(name = "PRICE")
    private BigDecimal price;

    /**
     * 合同经办人
     */
    @Column(name = "JBR")
    private String jbr;

    /**
     * 主办单位意见
     */
    @Column(name = "ZBDWYJ")
    private String zbdwyj;

    /**
     * 对方资质审查
     */
    @Column(name = "ZZSC")
    private String zzsc;

    /**
     * 评审结论
     */
    @Column(name = "PSJL")
    private String psjl;

    /**
     * 单位意见
     */
    @Column(name = "DWYJ")
    private String dwyj;

    /**
     * 财务部门意见
     */
    @Column(name = "CWBMYJ")
    private String cwbmyj;

    /**
     * 分管领导意见
     */
    @Column(name = "FGLDYJ")
    private String fgldyj;

    /**
     * 总经理意见
     */
    @Column(name = "ZJLYJ")
    private String zjlyj;

    /**
     * 合同日期
     */
    @Column(name = "RQ")
    private String rq;


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
     * 获取合同编号
     *
     * @return CONTRACT_NO - 合同编号
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * 设置合同编号
     *
     * @param contractNo 合同编号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * 获取合同项目
     *
     * @return PROJECT_ID - 合同项目
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * 设置合同项目
     *
     * @param projectId 合同项目
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取对方当事人
     *
     * @return DFDSR - 对方当事人
     */
    public String getDfdsr() {
        return dfdsr;
    }

    /**
     * 设置对方当事人
     *
     * @param dfdsr 对方当事人
     */
    public void setDfdsr(String dfdsr) {
        this.dfdsr = dfdsr;
    }

    /**
     * 获取投资文号
     *
     * @return TZWH - 投资文号
     */
    public String getTzwh() {
        return tzwh;
    }

    /**
     * 设置投资文号
     *
     * @param tzwh 投资文号
     */
    public void setTzwh(String tzwh) {
        this.tzwh = tzwh;
    }

    /**
     * 获取合同价款
     *
     * @return PRICE - 合同价款
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置合同价款
     *
     * @param price 合同价款
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取合同经办人
     *
     * @return JBR - 合同经办人
     */
    public String getJbr() {
        return jbr;
    }

    /**
     * 设置合同经办人
     *
     * @param jbr 合同经办人
     */
    public void setJbr(String jbr) {
        this.jbr = jbr;
    }

    /**
     * 获取主办单位意见
     *
     * @return ZBDWYJ - 主办单位意见
     */
    public String getZbdwyj() {
        return zbdwyj;
    }

    /**
     * 设置主办单位意见
     *
     * @param zbdwyj 主办单位意见
     */
    public void setZbdwyj(String zbdwyj) {
        this.zbdwyj = zbdwyj;
    }

    /**
     * 获取对方资质审查
     *
     * @return ZZSC - 对方资质审查
     */
    public String getZzsc() {
        return zzsc;
    }

    /**
     * 设置对方资质审查
     *
     * @param zzsc 对方资质审查
     */
    public void setZzsc(String zzsc) {
        this.zzsc = zzsc;
    }

    /**
     * 获取评审结论
     *
     * @return PSJL - 评审结论
     */
    public String getPsjl() {
        return psjl;
    }

    /**
     * 设置评审结论
     *
     * @param psjl 评审结论
     */
    public void setPsjl(String psjl) {
        this.psjl = psjl;
    }

    /**
     * 获取单位意见
     *
     * @return DWYJ - 单位意见
     */
    public String getDwyj() {
        return dwyj;
    }

    /**
     * 设置单位意见
     *
     * @param dwyj 单位意见
     */
    public void setDwyj(String dwyj) {
        this.dwyj = dwyj;
    }

    /**
     * 获取财务部门意见
     *
     * @return CWBMYJ - 财务部门意见
     */
    public String getCwbmyj() {
        return cwbmyj;
    }

    /**
     * 设置财务部门意见
     *
     * @param cwbmyj 财务部门意见
     */
    public void setCwbmyj(String cwbmyj) {
        this.cwbmyj = cwbmyj;
    }

    /**
     * 获取分管领导意见
     *
     * @return FGLDYJ - 分管领导意见
     */
    public String getFgldyj() {
        return fgldyj;
    }

    /**
     * 设置分管领导意见
     *
     * @param fgldyj 分管领导意见
     */
    public void setFgldyj(String fgldyj) {
        this.fgldyj = fgldyj;
    }

    /**
     * 获取总经理意见
     *
     * @return ZJLYJ - 总经理意见
     */
    public String getZjlyj() {
        return zjlyj;
    }

    /**
     * 设置总经理意见
     *
     * @param zjlyj 总经理意见
     */
    public void setZjlyj(String zjlyj) {
        this.zjlyj = zjlyj;
    }


    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }
}