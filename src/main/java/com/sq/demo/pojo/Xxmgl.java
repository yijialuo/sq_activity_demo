package com.sq.demo.pojo;

import java.util.Date;
import javax.persistence.*;

public class Xxmgl {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 项目编号
     */
    private String xmbh;

    /**
     * 项目名称
     */
    private String xmmc;

    /**
     * 立项部门
     */
    private String lxbm;

    /**
     * 申请人
     */
    private String sqr;

    /**
     * 创建时间
     */
    private String cjsj;

    /**
     * 多余字段一
     */
    private String y1;

    /**
     * 多余字段二
     */
    private String y2;

    /**
     * 多余字段三
     */
    private String y3;

    /**
     * 获取主键
     *
     * @return id - 主键
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
     * 获取项目编号
     *
     * @return xmbh - 项目编号
     */
    public String getXmbh() {
        return xmbh;
    }

    /**
     * 设置项目编号
     *
     * @param xmbh 项目编号
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
     * 获取立项部门
     *
     * @return lxbm - 立项部门
     */
    public String getLxbm() {
        return lxbm;
    }

    /**
     * 设置立项部门
     *
     * @param lxbm 立项部门
     */
    public void setLxbm(String lxbm) {
        this.lxbm = lxbm;
    }

    /**
     * 获取申请人
     *
     * @return sqr - 申请人
     */
    public String getSqr() {
        return sqr;
    }

    /**
     * 设置申请人
     *
     * @param sqr 申请人
     */
    public void setSqr(String sqr) {
        this.sqr = sqr;
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
     * 获取多余字段一
     *
     * @return y1 - 多余字段一
     */
    public String getY1() {
        return y1;
    }

    /**
     * 设置多余字段一
     *
     * @param y1 多余字段一
     */
    public void setY1(String y1) {
        this.y1 = y1;
    }

    /**
     * 获取多余字段二
     *
     * @return y2 - 多余字段二
     */
    public String getY2() {
        return y2;
    }

    /**
     * 设置多余字段二
     *
     * @param y2 多余字段二
     */
    public void setY2(String y2) {
        this.y2 = y2;
    }

    /**
     * 获取多余字段三
     *
     * @return y3 - 多余字段三
     */
    public String getY3() {
        return y3;
    }

    /**
     * 设置多余字段三
     *
     * @param y3 多余字段三
     */
    public void setY3(String y3) {
        this.y3 = y3;
    }
}