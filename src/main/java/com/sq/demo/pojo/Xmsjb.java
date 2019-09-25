package com.sq.demo.pojo;

import javax.persistence.*;

public class Xmsjb {
    /**
     * 项目id
     */
    @Id
    @Column(name = "projectId")
    private String projectid;

    /**
     * 创建时间
     */
    private String cjsj;

    /**
     * 前期开始时间
     */
    private String qqkssj;

    /**
     * 前期结束时间
     */
    private String qqjssj;

    /**
     * 招标开始时间
     */
    private String zbkssj;

    /**
     * 招标结束时间
     */
    private String zbjssj;

    /**
     * 合同开始时间
     */
    private String htkssj;

    /**
     * 合同结束时间
     */
    private String htjssj;

    /**
     * 施工开始时间
     */
    private String sgkssj;

    /**
     * 施工结束时间
     */
    private String sgjssj;

    /**
     * 验收开始时间
     */
    private String yskssj;

    /**
     * 验收结束时间
     */
    private String ysjssj;

    /**
     * 结算开始时间
     */
    private String jskssj;

    /**
     * 结算结算时间
     */
    private String jsjssj;

    /**
     * 获取项目id
     *
     * @return projectId - 项目id
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
     * 获取创建时间
     *
     * @return cjsj - 创建时间
     */
    public String getCjsj() {
        return cjsj;
    }

    /**
     * 设置创建时间
     *
     * @param cjsj 创建时间
     */
    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    /**
     * 获取前期开始时间
     *
     * @return qqkssj - 前期开始时间
     */
    public String getQqkssj() {
        return qqkssj;
    }

    /**
     * 设置前期开始时间
     *
     * @param qqkssj 前期开始时间
     */
    public void setQqkssj(String qqkssj) {
        this.qqkssj = qqkssj;
    }

    /**
     * 获取前期结束时间
     *
     * @return qqjssj - 前期结束时间
     */
    public String getQqjssj() {
        return qqjssj;
    }

    /**
     * 设置前期结束时间
     *
     * @param qqjssj 前期结束时间
     */
    public void setQqjssj(String qqjssj) {
        this.qqjssj = qqjssj;
    }

    /**
     * 获取招标开始时间
     *
     * @return zbkssj - 招标开始时间
     */
    public String getZbkssj() {
        return zbkssj;
    }

    /**
     * 设置招标开始时间
     *
     * @param zbkssj 招标开始时间
     */
    public void setZbkssj(String zbkssj) {
        this.zbkssj = zbkssj;
    }

    /**
     * 获取招标结束时间
     *
     * @return zbjssj - 招标结束时间
     */
    public String getZbjssj() {
        return zbjssj;
    }

    /**
     * 设置招标结束时间
     *
     * @param zbjssj 招标结束时间
     */
    public void setZbjssj(String zbjssj) {
        this.zbjssj = zbjssj;
    }

    /**
     * 获取合同开始时间
     *
     * @return htkssj - 合同开始时间
     */
    public String getHtkssj() {
        return htkssj;
    }

    /**
     * 设置合同开始时间
     *
     * @param htkssj 合同开始时间
     */
    public void setHtkssj(String htkssj) {
        this.htkssj = htkssj;
    }

    /**
     * 获取合同结束时间
     *
     * @return htjssj - 合同结束时间
     */
    public String getHtjssj() {
        return htjssj;
    }

    /**
     * 设置合同结束时间
     *
     * @param htjssj 合同结束时间
     */
    public void setHtjssj(String htjssj) {
        this.htjssj = htjssj;
    }

    /**
     * 获取施工开始时间
     *
     * @return sgkssj - 施工开始时间
     */
    public String getSgkssj() {
        return sgkssj;
    }

    /**
     * 设置施工开始时间
     *
     * @param sgkssj 施工开始时间
     */
    public void setSgkssj(String sgkssj) {
        this.sgkssj = sgkssj;
    }

    /**
     * 获取施工结束时间
     *
     * @return sgjssj - 施工结束时间
     */
    public String getSgjssj() {
        return sgjssj;
    }

    /**
     * 设置施工结束时间
     *
     * @param sgjssj 施工结束时间
     */
    public void setSgjssj(String sgjssj) {
        this.sgjssj = sgjssj;
    }

    /**
     * 获取验收开始时间
     *
     * @return yskssj - 验收开始时间
     */
    public String getYskssj() {
        return yskssj;
    }

    /**
     * 设置验收开始时间
     *
     * @param yskssj 验收开始时间
     */
    public void setYskssj(String yskssj) {
        this.yskssj = yskssj;
    }

    /**
     * 获取验收结束时间
     *
     * @return ysjssj - 验收结束时间
     */
    public String getYsjssj() {
        return ysjssj;
    }

    /**
     * 设置验收结束时间
     *
     * @param ysjssj 验收结束时间
     */
    public void setYsjssj(String ysjssj) {
        this.ysjssj = ysjssj;
    }

    /**
     * 获取结算开始时间
     *
     * @return jskssj - 结算开始时间
     */
    public String getJskssj() {
        return jskssj;
    }

    /**
     * 设置结算开始时间
     *
     * @param jskssj 结算开始时间
     */
    public void setJskssj(String jskssj) {
        this.jskssj = jskssj;
    }

    /**
     * 获取结算结算时间
     *
     * @return jsjssj - 结算结算时间
     */
    public String getJsjssj() {
        return jsjssj;
    }

    /**
     * 设置结算结算时间
     *
     * @param jsjssj 结算结算时间
     */
    public void setJsjssj(String jsjssj) {
        this.jsjssj = jsjssj;
    }
}