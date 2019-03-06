package com.sq.demo.pojo;

import java.math.BigDecimal;
import javax.persistence.*;

public class Payable {
    /**
     * 主键
     */
    @Column(name = "ID")
    private String id;

    /**
     * 经办部门
     */
    @Column(name = "JBBM")
    private String jbbm;

    /**
     * 验收证明人
     */
    @Column(name = "YSZMR")
    private String yszmr;

    /**
     * 发票号码
     */
    @Column(name = "INVOICE_NO")
    private String invoiceNo;

    /**
     * 银行账号
     */
    @Column(name = "ACCOUNT")
    private String account;

    /**
     * 开户银行
     */
    @Column(name = "BANK")
    private String bank;

    /**
     * 支付项目
     */
    @Column(name = "PROJECT")
    private String project;

    /**
     * 合同id
     */
    @Column(name = "CONTRACT_ID")
    private String contractId;

    /**
     * 本期应付
     */
    @Column(name = "BQYF")
    private BigDecimal bqyf;

    /**
     * 经办部门负责人
     */
    @Column(name = "JBBMFZR")
    private String jbbmfzr;

    /**
     * 主管部门负责人
     */
    @Column(name = "ZGBMFZR")
    private String zgbmfzr;

    /**
     * 财务总监
     */
    @Column(name = "CWZJ")
    private String cwzj;

    /**
     * 分管领导
     */
    @Column(name = "FGLD")
    private String fgld;

    /**
     * 公司总经理
     */
    @Column(name = "GSZJL")
    private String gszjl;

    /**
     * 经办人
     */
    @Column(name = "JBR")
    private String jbr;

    /**
     * 日期
     */
    @Column(name = "RQ")
    private String rq;

    /**
     * 未支付账款
     */
    @Column(name = "WZF")
    private BigDecimal wzf;

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
     * 获取经办部门
     *
     * @return JBBM - 经办部门
     */
    public String getJbbm() {
        return jbbm;
    }

    /**
     * 设置经办部门
     *
     * @param jbbm 经办部门
     */
    public void setJbbm(String jbbm) {
        this.jbbm = jbbm;
    }

    /**
     * 获取验收证明人
     *
     * @return YSZMR - 验收证明人
     */
    public String getYszmr() {
        return yszmr;
    }

    /**
     * 设置验收证明人
     *
     * @param yszmr 验收证明人
     */
    public void setYszmr(String yszmr) {
        this.yszmr = yszmr;
    }

    /**
     * 获取发票号码
     *
     * @return INVOICE_NO - 发票号码
     */
    public String getInvoiceNo() {
        return invoiceNo;
    }

    /**
     * 设置发票号码
     *
     * @param invoiceNo 发票号码
     */
    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    /**
     * 获取银行账号
     *
     * @return ACCOUNT - 银行账号
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置银行账号
     *
     * @param account 银行账号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取开户银行
     *
     * @return BANK - 开户银行
     */
    public String getBank() {
        return bank;
    }

    /**
     * 设置开户银行
     *
     * @param bank 开户银行
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 获取支付项目
     *
     * @return PROJECT - 支付项目
     */
    public String getProject() {
        return project;
    }

    /**
     * 设置支付项目
     *
     * @param project 支付项目
     */
    public void setProject(String project) {
        this.project = project;
    }

    /**
     * 获取合同id
     *
     * @return CONTRACT_ID - 合同id
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * 设置合同id
     *
     * @param contractId 合同id
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }

    /**
     * 获取本期应付
     *
     * @return BQYF - 本期应付
     */
    public BigDecimal getBqyf() {
        return bqyf;
    }

    /**
     * 设置本期应付
     *
     * @param bqyf 本期应付
     */
    public void setBqyf(BigDecimal bqyf) {
        this.bqyf = bqyf;
    }

    /**
     * 获取经办部门负责人
     *
     * @return JBBMFZR - 经办部门负责人
     */
    public String getJbbmfzr() {
        return jbbmfzr;
    }

    /**
     * 设置经办部门负责人
     *
     * @param jbbmfzr 经办部门负责人
     */
    public void setJbbmfzr(String jbbmfzr) {
        this.jbbmfzr = jbbmfzr;
    }

    /**
     * 获取主管部门负责人
     *
     * @return ZGBMFZR - 主管部门负责人
     */
    public String getZgbmfzr() {
        return zgbmfzr;
    }

    /**
     * 设置主管部门负责人
     *
     * @param zgbmfzr 主管部门负责人
     */
    public void setZgbmfzr(String zgbmfzr) {
        this.zgbmfzr = zgbmfzr;
    }

    /**
     * 获取财务总监
     *
     * @return CWZJ - 财务总监
     */
    public String getCwzj() {
        return cwzj;
    }

    /**
     * 设置财务总监
     *
     * @param cwzj 财务总监
     */
    public void setCwzj(String cwzj) {
        this.cwzj = cwzj;
    }

    /**
     * 获取分管领导
     *
     * @return FGLD - 分管领导
     */
    public String getFgld() {
        return fgld;
    }

    /**
     * 设置分管领导
     *
     * @param fgld 分管领导
     */
    public void setFgld(String fgld) {
        this.fgld = fgld;
    }

    /**
     * 获取公司总经理
     *
     * @return GSZJL - 公司总经理
     */
    public String getGszjl() {
        return gszjl;
    }

    /**
     * 设置公司总经理
     *
     * @param gszjl 公司总经理
     */
    public void setGszjl(String gszjl) {
        this.gszjl = gszjl;
    }

    /**
     * 获取经办人
     *
     * @return JBR - 经办人
     */
    public String getJbr() {
        return jbr;
    }

    /**
     * 设置经办人
     *
     * @param jbr 经办人
     */
    public void setJbr(String jbr) {
        this.jbr = jbr;
    }

    /**
     * 获取日期
     *
     * @return RQ - 日期
     */
    public String getRq() {
        return rq;
    }

    /**
     * 设置日期
     *
     * @param rq 日期
     */
    public void setRq(String rq) {
        this.rq = rq;
    }

    /**
     * 获取未支付账款
     *
     * @return WZF - 未支付账款
     */
    public BigDecimal getWzf() {
        return wzf;
    }

    /**
     * 设置未支付账款
     *
     * @param wzf 未支付账款
     */
    public void setWzf(BigDecimal wzf) {
        this.wzf = wzf;
    }
}