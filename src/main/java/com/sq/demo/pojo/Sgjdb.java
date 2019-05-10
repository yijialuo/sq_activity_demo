package com.sq.demo.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sq.demo.Entity.Sgjdb2;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Sgjdb {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 编号
     */
    @Column(name = "PROJECT_NO")
    private String projectNo;

    /**
     * 项目名称
     */
    @Column(name = "PROJECT_NAM")
    private String projectNam;

    /**
     * 投资概算
     */
    @Column(name = "INVESTMENT_ESTIMATE")
    private String investmentEstimate;

    /**
     * 申报部门
     */
    @Column(name = "DECLARATION_DEP")
    private String declarationDep;

    @Column(name = "lxlb")
    private String lxlb;

    /**
     * 修改人 项目分类
     */
    @Column(name = "xmlb")
    private String xmlb;

    @Column(name = "xmdl")
    private String xmdl;

    private String jsbjlsj;

    /**
     * 两会时间
     */
    @Column(name = "LHSJ")
    private String lhsj;

    /**
     * 总经会时间
     */
    @Column(name = "ZJHSJ")
    private String zjhsj;

    /**
     * 定标时间
     */
    private String dbsj;

    /**
     * 创建时间
     */
    private String httjpssj;

    /**
     * 日期
     */
    private String htqdsj;

    /**
     * 合同价款
     */
    private BigDecimal htje;

    /**
     * 时间节点
     */
    private String kgsj;

    /**
     * 验收日期
     */
    @Column(name = "YSRQ")
    private String ysrq;

    private BigDecimal bndjsjd;

    private BigDecimal zjsjd;

    /**
     * 日期
     */
    private String wcjsjs;

    private String jsbzgjl;

    @Column(name = "jsbjbr")
    private String jsbjbr;

    /**
     * 中标单位
     */
    private String sgdw;

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
     * 获取编号
     *
     * @return PROJECT_NO - 编号
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * 设置编号
     *
     * @param projectNo 编号
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    /**
     * 获取项目名称
     *
     * @return PROJECT_NAM - 项目名称
     */
    public String getProjectNam() {
        return projectNam;
    }

    /**
     * 设置项目名称
     *
     * @param projectNam 项目名称
     */
    public void setProjectNam(String projectNam) {
        this.projectNam = projectNam;
    }

    /**
     * 获取投资概算
     *
     * @return INVESTMENT_ESTIMATE - 投资概算
     */
    public String getInvestmentEstimate() {
        return investmentEstimate;
    }

    /**
     * 设置投资概算
     *
     * @param investmentEstimate 投资概算
     */
    public void setInvestmentEstimate(String investmentEstimate) {
        this.investmentEstimate = investmentEstimate;
    }

    /**
     * 获取申报部门
     *
     * @return DECLARATION_DEP - 申报部门
     */
    public String getDeclarationDep() {
        return declarationDep;
    }

    /**
     * 设置申报部门
     *
     * @param declarationDep 申报部门
     */
    public void setDeclarationDep(String declarationDep) {
        this.declarationDep = declarationDep;
    }


    /**
     * 获取两会时间
     *
     * @return LHSJ - 两会时间
     */
    public String getLhsj() {
        return lhsj;
    }

    /**
     * 设置两会时间
     *
     * @param lhsj 两会时间
     */
    public void setLhsj(String lhsj) {
        this.lhsj = lhsj;
    }

    /**
     * 获取总经会时间
     *
     * @return ZJHSJ - 总经会时间
     */
    public String getZjhsj() {
        return zjhsj;
    }

    /**
     * 设置总经会时间
     *
     * @param zjhsj 总经会时间
     */
    public void setZjhsj(String zjhsj) {
        this.zjhsj = zjhsj;
    }

    /**
     * 获取定标时间
     *
     * @return dbsj - 定标时间
     */
    public String getDbsj() {
        return dbsj;
    }

    /**
     * 设置定标时间
     *
     * @param dbsj 定标时间
     */
    public void setDbsj(String dbsj) {
        this.dbsj = dbsj;
    }

    /**
     * 获取创建时间
     *
     * @return httjpssj - 创建时间
     */
    public String getHttjpssj() {
        return httjpssj;
    }

    /**
     * 设置创建时间
     *
     * @param httjpssj 创建时间
     */
    public void setHttjpssj(String httjpssj) {
        this.httjpssj = httjpssj;
    }

    /**
     * 获取日期
     *
     * @return htqdsj - 日期
     */
    public String getHtqdsj() {
        return htqdsj;
    }

    /**
     * 设置日期
     *
     * @param htqdsj 日期
     */
    public void setHtqdsj(String htqdsj) {
        this.htqdsj = htqdsj;
    }

    /**
     * 获取合同价款
     *
     * @return htje - 合同价款
     */
    public BigDecimal getHtje() {
        return htje;
    }

    /**
     * 设置合同价款
     *
     * @param htje 合同价款
     */
    public void setHtje(BigDecimal htje) {
        this.htje = htje;
    }

    /**
     * 获取时间节点
     *
     * @return kgsj - 时间节点
     */
    public String getKgsj() {
        return kgsj;
    }

    /**
     * 设置时间节点
     *
     * @param kgsj 时间节点
     */
    public void setKgsj(String kgsj) {
        this.kgsj = kgsj;
    }

    /**
     * 获取验收日期
     *
     * @return YSRQ - 验收日期
     */
    public String getYsrq() {
        return ysrq;
    }

    /**
     * 设置验收日期
     *
     * @param ysrq 验收日期
     */
    public void setYsrq(String ysrq) {
        this.ysrq = ysrq;
    }

    /**
     * @return bndjsjd
     */
    public BigDecimal getBndjsjd() {
        return bndjsjd;
    }

    /**
     * @param bndjsjd
     */
    public void setBndjsjd(BigDecimal bndjsjd) {
        this.bndjsjd = bndjsjd;
    }

    /**
     * @return zjsjd
     */
    public BigDecimal getZjsjd() {
        return zjsjd;
    }

    /**
     * @param zjsjd
     */
    public void setZjsjd(BigDecimal zjsjd) {
        this.zjsjd = zjsjd;
    }

    /**
     * 获取日期
     *
     * @return wcjsjs - 日期
     */
    public String getWcjsjs() {
        return wcjsjs;
    }

    /**
     * 设置日期
     *
     * @param wcjsjs 日期
     */
    public void setWcjsjs(String wcjsjs) {
        this.wcjsjs = wcjsjs;
    }

    /**
     * @return jsbzgjl
     */
    public String getJsbzgjl() {
        return jsbzgjl;
    }

    /**
     * @param jsbzgjl
     */
    public void setJsbzgjl(String jsbzgjl) {
        this.jsbzgjl = jsbzgjl;
    }

    /**
     * 获取中标单位
     *
     * @return sgdw - 中标单位
     */
    public String getSgdw() {
        return sgdw;
    }

    /**
     * 设置中标单位
     *
     * @param sgdw 中标单位
     */
    public void setSgdw(String sgdw) {
        this.sgdw = sgdw;
    }

    public String getXmlb() {
        return xmlb;
    }

    public void setXmlb(String xmlb) {
        this.xmlb = xmlb;
    }

    public String getXmdl() {
        return xmdl;
    }

    public void setXmdl(String xmdl) {
        this.xmdl = xmdl;
    }

    public String getLxlb() {
        return lxlb;
    }

    public void setLxlb(String lxlb) {
        this.lxlb = lxlb;
    }

    public String getJsbjbr() {
        return jsbjbr;
    }

    public void setJsbjbr(String jsbjbr) {
        this.jsbjbr = jsbjbr;
    }

    public String getJsbjlsj() {
        return jsbjlsj;
    }

    public void setJsbjlsj(String jsbjlsj) {
        this.jsbjlsj = jsbjlsj;
    }
}