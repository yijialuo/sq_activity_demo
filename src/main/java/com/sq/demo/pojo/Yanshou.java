package com.sq.demo.pojo;

import javax.persistence.*;

public class Yanshou {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 验收编号
     */
    @Column(name = "YSNO")
    private String ysno;

    /**
     * 项目id
     */
    @Column(name = "PROJECTID")
    private String projectid;

    /**
     * 建设地点
     */
    @Column(name = "JSDD")
    private String jsdd;

    /**
     * 使用部门
     */
    @Column(name = "SYBM")
    private String sybm;

    /**
     * 施工单位
     */
    @Column(name = "SGDW")
    private String sgdw;

    /**
     * 计划文号
     */
    @Column(name = "JHWH")
    private String jhwh;

    /**
     * 开工日期
     */
    @Column(name = "KGRQ")
    private String kgrq;

    /**
     * 计划金额
     */
    @Column(name = "JHJE")
    private String jhje;

    /**
     * 实际竣工日期
     */
    @Column(name = "SJJGRQ")
    private String sjjgrq;

    /**
     * 承包金额
     */
    @Column(name = "CBJE")
    private String cbje;

    /**
     * 验收日期
     */
    @Column(name = "YSRQ")
    private String ysrq;

    /**
     * 施工单位自我评定
     */
    @Column(name = "SGDWZWPD")
    private String sgdwzwpd;

    /**
     * 使用部门验收意见
     */
    @Column(name = "SYBMYSYJ")
    private String sybmysyj;

    /**
     * 项目主管部门质量评定
     */
    @Column(name = "ZGBMPD")
    private String zgbmpd;

    /**
     * 参加验收人员验收结论
     */
    @Column(name = "YSJL")
    private String ysjl;

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
     * 获取验收编号
     *
     * @return YSNO - 验收编号
     */
    public String getYsno() {
        return ysno;
    }

    /**
     * 设置验收编号
     *
     * @param ysno 验收编号
     */
    public void setYsno(String ysno) {
        this.ysno = ysno;
    }
    /*
     * 获取建设地点
     *
     * @return JSDD - 建设地点
     */
    public String getJsdd() {
        return jsdd;
    }

    /**
     * 设置建设地点
     *
     * @param jsdd 建设地点
     */
    public void setJsdd(String jsdd) {
        this.jsdd = jsdd;
    }

    /**
     * 获取使用部门
     *
     * @return SYBM - 使用部门
     */
    public String getSybm() {
        return sybm;
    }

    /**
     * 设置使用部门
     *
     * @param sybm 使用部门
     */
    public void setSybm(String sybm) {
        this.sybm = sybm;
    }

    /**
     * 获取施工单位
     *
     * @return SGDW - 施工单位
     */
    public String getSgdw() {
        return sgdw;
    }

    /**
     * 设置施工单位
     *
     * @param sgdw 施工单位
     */
    public void setSgdw(String sgdw) {
        this.sgdw = sgdw;
    }

    /**
     * 获取计划文号
     *
     * @return JHWH - 计划文号
     */
    public String getJhwh() {
        return jhwh;
    }

    /**
     * 设置计划文号
     *
     * @param jhwh 计划文号
     */
    public void setJhwh(String jhwh) {
        this.jhwh = jhwh;
    }

    /**
     * 获取开工日期
     *
     * @return KGRQ - 开工日期
     */
    public String getKgrq() {
        return kgrq;
    }

    /**
     * 设置开工日期
     *
     * @param kgrq 开工日期
     */
    public void setKgrq(String kgrq) {
        this.kgrq = kgrq;
    }

    /**
     * 获取计划金额
     *
     * @return JHJE - 计划金额
     */
    public String getJhje() {
        return jhje;
    }

    /**
     * 设置计划金额
     *
     * @param jhje 计划金额
     */
    public void setJhje(String jhje) {
        this.jhje = jhje;
    }

    /**
     * 获取实际竣工日期
     *
     * @return SJJGRQ - 实际竣工日期
     */
    public String getSjjgrq() {
        return sjjgrq;
    }

    /**
     * 设置实际竣工日期
     *
     * @param sjjgrq 实际竣工日期
     */
    public void setSjjgrq(String sjjgrq) {
        this.sjjgrq = sjjgrq;
    }

    /**
     * 获取承包金额
     *
     * @return CBJE - 承包金额
     */
    public String getCbje() {
        return cbje;
    }

    /**
     * 设置承包金额
     *
     * @param cbje 承包金额
     */
    public void setCbje(String cbje) {
        this.cbje = cbje;
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
     * 获取施工单位自我评定
     *
     * @return SGDWZWPD - 施工单位自我评定
     */
    public String getSgdwzwpd() {
        return sgdwzwpd;
    }

    /**
     * 设置施工单位自我评定
     *
     * @param sgdwzwpd 施工单位自我评定
     */
    public void setSgdwzwpd(String sgdwzwpd) {
        this.sgdwzwpd = sgdwzwpd;
    }

    /**
     * 获取使用部门验收意见
     *
     * @return SYBMYSYJ - 使用部门验收意见
     */
    public String getSybmysyj() {
        return sybmysyj;
    }

    /**
     * 设置使用部门验收意见
     *
     * @param sybmysyj 使用部门验收意见
     */
    public void setSybmysyj(String sybmysyj) {
        this.sybmysyj = sybmysyj;
    }

    /**
     * 获取项目主管部门质量评定
     *
     * @return ZGBMPD - 项目主管部门质量评定
     */
    public String getZgbmpd() {
        return zgbmpd;
    }

    /**
     * 设置项目主管部门质量评定
     *
     * @param zgbmpd 项目主管部门质量评定
     */
    public void setZgbmpd(String zgbmpd) {
        this.zgbmpd = zgbmpd;
    }

    /**
     * 获取参加验收人员验收结论
     *
     * @return YSJL - 参加验收人员验收结论
     */
    public String getYsjl() {
        return ysjl;
    }

    /**
     * 设置参加验收人员验收结论
     *
     * @param ysjl 参加验收人员验收结论
     */
    public void setYsjl(String ysjl) {
        this.ysjl = ysjl;
    }


    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }
}