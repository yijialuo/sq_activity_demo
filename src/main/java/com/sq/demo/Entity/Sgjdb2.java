package com.sq.demo.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

public class Sgjdb2 {


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
    private String[] declarationDep;

    @Column(name = "lxlb")
    private String[] lxlb;

    /**
     * 修改人 项目分类
     */
    @Column(name = "xmlb")
    private String[] xmlb;

    @Column(name = "xmdl")
    private String[] xmdl;


    private String ksjsbjlsj;
    private String jsjsbjlsj;

    /**
     * 两会时间
     */
    @Column(name = "LHSJ")
    private String kslhsj;
    private String jslhsj;

    /**
     * 总经会时间
     */
    @Column(name = "ZJHSJ")
    private String kszjhsj;
    private String jszjhsj;

    /**
     * 定标时间
     */
    private String ksdbsj;
    private String jsdbsj;

    /**
     * 创建时间
     */

    private String kshttjpssj;

    private String jshttjpssj;

    /**
     * 日期
     */
    private String kshtqdsj;
    private String jshtqdsj;

    /**
     * 合同价款
     */
    private BigDecimal htje;

    /**
     * 时间节点
     */
    private String kskgsj;
    private String jskgsj;

    /**
     * 验收日期
     */
    @Column(name = "YSRQ")
    private String ksysrq;
    private String jsysrq;

    private BigDecimal bndjsjd;

    private BigDecimal zjsjd;

    /**
     * 日期
     */
    private String kswcjsjs;
    private String jswcjsjs;

    private String jsbzgjl;

    @Column(name = "jsbjbr")
    private String[] jsbjbr;

    /**
     * 中标单位
     */
    private String sgdw;

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getProjectNam() {
        return projectNam;
    }

    public void setProjectNam(String projectNam) {
        this.projectNam = projectNam;
    }

    public String getInvestmentEstimate() {
        return investmentEstimate;
    }

    public void setInvestmentEstimate(String investmentEstimate) {
        this.investmentEstimate = investmentEstimate;
    }

    public String[] getDeclarationDep() {
        return declarationDep;
    }

    public void setDeclarationDep(String[] declarationDep) {
        this.declarationDep = declarationDep;
    }

    public String[] getLxlb() {
        return lxlb;
    }

    public void setLxlb(String[] lxlb) {
        this.lxlb = lxlb;
    }

    public String[] getXmlb() {
        return xmlb;
    }

    public void setXmlb(String[] xmlb) {
        this.xmlb = xmlb;
    }

    public String[] getXmdl() {
        return xmdl;
    }

    public void setXmdl(String[] xmdl) {
        this.xmdl = xmdl;
    }

    public String getKsjsbjlsj() {
        return ksjsbjlsj;
    }

    public void setKsjsbjlsj(String ksjsbjlsj) {
        this.ksjsbjlsj = ksjsbjlsj;
    }

    public String getJsjsbjlsj() {
        return jsjsbjlsj;
    }

    public void setJsjsbjlsj(String jsjsbjlsj) {
        this.jsjsbjlsj = jsjsbjlsj;
    }

    public String getKslhsj() {
        return kslhsj;
    }

    public void setKslhsj(String kslhsj) {
        this.kslhsj = kslhsj;
    }

    public String getJslhsj() {
        return jslhsj;
    }

    public void setJslhsj(String jslhsj) {
        this.jslhsj = jslhsj;
    }

    public String getKszjhsj() {
        return kszjhsj;
    }

    public void setKszjhsj(String kszjhsj) {
        this.kszjhsj = kszjhsj;
    }

    public String getJszjhsj() {
        return jszjhsj;
    }

    public void setJszjhsj(String jszjhsj) {
        this.jszjhsj = jszjhsj;
    }

    public String getKsdbsj() {
        return ksdbsj;
    }

    public void setKsdbsj(String ksdbsj) {
        this.ksdbsj = ksdbsj;
    }

    public String getJsdbsj() {
        return jsdbsj;
    }

    public void setJsdbsj(String jsdbsj) {
        this.jsdbsj = jsdbsj;
    }

    public String getKshttjpssj() {
        return kshttjpssj;
    }

    public void setKshttjpssj(String kshttjpssj) {
        this.kshttjpssj = kshttjpssj;
    }

    public String getJshttjpssj() {
        return jshttjpssj;
    }

    public void setJshttjpssj(String jshttjpssj) {
        this.jshttjpssj = jshttjpssj;
    }

    public String getKshtqdsj() {
        return kshtqdsj;
    }

    public void setKshtqdsj(String kshtqdsj) {
        this.kshtqdsj = kshtqdsj;
    }

    public String getJshtqdsj() {
        return jshtqdsj;
    }

    public void setJshtqdsj(String jshtqdsj) {
        this.jshtqdsj = jshtqdsj;
    }

    public BigDecimal getHtje() {
        return htje;
    }

    public void setHtje(BigDecimal htje) {
        this.htje = htje;
    }

    public String getKskgsj() {
        return kskgsj;
    }

    public void setKskgsj(String kskgsj) {
        this.kskgsj = kskgsj;
    }

    public String getJskgsj() {
        return jskgsj;
    }

    public void setJskgsj(String jskgsj) {
        this.jskgsj = jskgsj;
    }

    public String getKsysrq() {
        return ksysrq;
    }

    public void setKsysrq(String ksysrq) {
        this.ksysrq = ksysrq;
    }

    public String getJsysrq() {
        return jsysrq;
    }

    public void setJsysrq(String jsysrq) {
        this.jsysrq = jsysrq;
    }

    public BigDecimal getBndjsjd() {
        return bndjsjd;
    }

    public void setBndjsjd(BigDecimal bndjsjd) {
        this.bndjsjd = bndjsjd;
    }

    public BigDecimal getZjsjd() {
        return zjsjd;
    }

    public void setZjsjd(BigDecimal zjsjd) {
        this.zjsjd = zjsjd;
    }

    public String getKswcjsjs() {
        return kswcjsjs;
    }

    public void setKswcjsjs(String kswcjsjs) {
        this.kswcjsjs = kswcjsjs;
    }

    public String getJswcjsjs() {
        return jswcjsjs;
    }

    public void setJswcjsjs(String jswcjsjs) {
        this.jswcjsjs = jswcjsjs;
    }

    public String getJsbzgjl() {
        return jsbzgjl;
    }

    public void setJsbzgjl(String jsbzgjl) {
        this.jsbzgjl = jsbzgjl;
    }

    public String[] getJsbjbr() {
        return jsbjbr;
    }

    public void setJsbjbr(String[] jsbjbr) {
        this.jsbjbr = jsbjbr;
    }

    public String getSgdw() {
        return sgdw;
    }

    public void setSgdw(String sgdw) {
        this.sgdw = sgdw;
    }
}