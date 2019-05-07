package com.sq.demo.Entity;

import javax.persistence.Id;
import java.math.BigDecimal;

public class Xmcxb2 {
    /**
     * 编号
     */
    private String xmbh;

    /**
     * 项目名称
     */
    private String xmmc;

    /**
     * 申请时间
     */
    private String lxsj;

    /**
     * 申报部门
     */
    private String[] lxbm;

    /**
     * 修改人 项目分类
     */
    private String[] xmdl;

    /**
     * 部门审核意见
     */
    private String[] lxlb;

    /**
     * 项目类型
     */
    private String[] xmlb;

    /**
     * 投资概算
     */
    private String jhje;

    /**
     * 合同价款
     */
    private BigDecimal htje;

    /**
     * 时间节点
     */
    private String kgsj;

    /**
     * 完成时间
     */
    private String wgsj;

    private BigDecimal zjsjd;

    private BigDecimal jnjsjd;

    /**
     * 两会时间
     */
    private String xmghsj;

    /**
     * 定标时间
     */
    private String dbsj;

    /**
     * 日期
     */
    private String htqdsj;

    /**
     * 日期
     */
    private String jssj;

    /**
     * 中标单位
     */
    private String cbdw;

    /**
     * 申请人
     */
    private String[] xmfqr;

    /**
     * 中标单位
     */
    private String[] xmjbr;

    private String spzt;

    private String[] htzt;

    private String[] sgzt;

    private String[] jszt;

    private String lhzbwjsj;

    /**
     * 获取编号
     *
     * @return xmbh - 编号
     */
    public String getXmbh() {
        return xmbh;
    }

    /**
     * 设置编号
     *
     * @param xmbh 编号
     */
    public void setXmbh(String xmbh) {
        this.xmbh = xmbh;
    }

    /**
     * 获取项目名称
     *
     * @return xmmc - 项目名称
     */
    public String getXmmc() {
        return xmmc;
    }

    /**
     * 设置项目名称
     *
     * @param xmmc 项目名称
     */
    public void setXmmc(String xmmc) {
        this.xmmc = xmmc;
    }

    /**
     * 获取申请时间
     *
     * @return lxsj - 申请时间
     */
    public String getLxsj() {
        return lxsj;
    }

    /**
     * 设置申请时间
     *
     * @param lxsj 申请时间
     */
    public void setLxsj(String lxsj) {
        this.lxsj = lxsj;
    }

    /**
     * 获取申报部门
     *
     * @return lxbm - 申报部门
     */
    public String[] getLxbm() {
        return lxbm;
    }

    /**
     * 设置申报部门
     *
     * @param lxbm 申报部门
     */
    public void setLxbm(String[] lxbm) {
        this.lxbm = lxbm;
    }

    /**
     * 获取修改人 项目分类
     *
     * @return xmdl - 修改人 项目分类
     */
    public String[] getXmdl() {
        return xmdl;
    }

    /**
     * 设置修改人 项目分类
     *
     * @param xmdl 修改人 项目分类
     */
    public void setXmdl(String[] xmdl) {
        this.xmdl = xmdl;
    }

    /**
     * 获取部门审核意见
     *
     * @return lxlb - 部门审核意见
     */
    public String[] getLxlb() {
        return lxlb;
    }

    /**
     * 设置部门审核意见
     *
     * @param lxlb 部门审核意见
     */
    public void setLxlb(String[] lxlb) {
        this.lxlb = lxlb;
    }

    /**
     * 获取项目类型
     *
     * @return xmlb - 项目类型
     */
    public String[] getXmlb() {
        return xmlb;
    }

    /**
     * 设置项目类型
     *
     * @param xmlb 项目类型
     */
    public void setXmlb(String[] xmlb) {
        this.xmlb = xmlb;
    }

    /**
     * 获取投资概算
     *
     * @return jhje - 投资概算
     */
    public String getJhje() {
        return jhje;
    }

    /**
     * 设置投资概算
     *
     * @param jhje 投资概算
     */
    public void setJhje(String jhje) {
        this.jhje = jhje;
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
     * 获取完成时间
     *
     * @return wgsj - 完成时间
     */
    public String getWgsj() {
        return wgsj;
    }

    /**
     * 设置完成时间
     *
     * @param wgsj 完成时间
     */
    public void setWgsj(String wgsj) {
        this.wgsj = wgsj;
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
     * @return jnjsjd
     */
    public BigDecimal getJnjsjd() {
        return jnjsjd;
    }

    /**
     * @param jnjsjd
     */
    public void setJnjsjd(BigDecimal jnjsjd) {
        this.jnjsjd = jnjsjd;
    }

    /**
     * 获取两会时间
     *
     * @return xmghsj - 两会时间
     */
    public String getXmghsj() {
        return xmghsj;
    }

    /**
     * 设置两会时间
     *
     * @param xmghsj 两会时间
     */
    public void setXmghsj(String xmghsj) {
        this.xmghsj = xmghsj;
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
     * 获取日期
     *
     * @return jssj - 日期
     */
    public String getJssj() {
        return jssj;
    }

    /**
     * 设置日期
     *
     * @param jssj 日期
     */
    public void setJssj(String jssj) {
        this.jssj = jssj;
    }

    /**
     * 获取中标单位
     *
     * @return cbdw - 中标单位
     */
    public String getCbdw() {
        return cbdw;
    }

    /**
     * 设置中标单位
     *
     * @param cbdw 中标单位
     */
    public void setCbdw(String cbdw) {
        this.cbdw = cbdw;
    }

    /**
     * 获取申请人
     *
     * @return xmfqr - 申请人
     */
    public String[] getXmfqr() {
        return xmfqr;
    }

    /**
     * 设置申请人
     *
     * @param xmfqr 申请人
     */
    public void setXmfqr(String[] xmfqr) {
        this.xmfqr = xmfqr;
    }

    /**
     * 获取中标单位
     *
     * @return xmjbr - 中标单位
     */
    public String[] getXmjbr() {
        return xmjbr;
    }

    /**
     * 设置中标单位
     *
     * @param xmjbr 中标单位
     */
    public void setXmjbr(String[] xmjbr) {
        this.xmjbr = xmjbr;
    }

    /**
     * @return spzt
     */
    public String getSpzt() {
        return spzt;
    }

    /**
     * @param spzt
     */
    public void setSpzt(String spzt) {
        this.spzt = spzt;
    }

    /**
     * @return htzt
     */
    public String[] getHtzt() {
        return htzt;
    }

    /**
     * @param htzt
     */
    public void setHtzt(String[] htzt) {
        this.htzt = htzt;
    }

    /**
     * @return sgzt
     */
    public String[] getSgzt() {
        return sgzt;
    }

    /**
     * @param sgzt
     */
    public void setSgzt(String[] sgzt) {
        this.sgzt = sgzt;
    }

    /**
     * @return jszt
     */
    public String[] getJszt() {
        return jszt;
    }

    /**
     * @param jszt
     */
    public void setJszt(String[] jszt) {
        this.jszt = jszt;
    }

    /**
     * @return lhzbwjsj
     */
    public String getLhzbwjsj() {
        return lhzbwjsj;
    }

    /**
     * @param lhzbwjsj
     */
    public void setLhzbwjsj(String lhzbwjsj) {
        this.lhzbwjsj = lhzbwjsj;
    }
}