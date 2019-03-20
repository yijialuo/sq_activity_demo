package com.sq.demo.pojo;

import javax.persistence.*;
import java.io.Serializable;

public class Zhaobiao implements Serializable {
    /**
     * 主键
     */
    @Id
    private String id;

    /**
     * 项目id
     */
    private String xmid;

    /**
     * 技术要求
     */
    private String jsyq;

    /**
     * 申请人
     */
    private String sqr;

    /**
     * 招标pid
     */
    private String zbpid;

    //发标时间
    private String fbsj;
    //评标时间
    private String pbsj;
    //投标截止时间   现在改为工期
    private String tbjzsj;
    //创建时间
    private String cjsj;
    //定标时间
    private String dbsj;

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
     * 获取项目id
     *
     * @return xmid - 项目id
     */
    public String getXmid() {
        return xmid;
    }

    /**
     * 设置项目id
     *
     * @param xmid 项目id
     */
    public void setXmid(String xmid) {
        this.xmid = xmid;
    }

    /**
     * 获取技术要求
     *
     * @return jsyq - 技术要求
     */
    public String getJsyq() {
        return jsyq;
    }

    /**
     * 设置技术要求
     *
     * @param jsyq 技术要求
     */
    public void setJsyq(String jsyq) {
        this.jsyq = jsyq;
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
     * 获取招标pid
     *
     * @return zbpid - 招标pid
     */
    public String getZbpid() {
        return zbpid;
    }

    /**
     * 设置招标pid
     *
     * @param zbpid 招标pid
     */
    public void setZbpid(String zbpid) {
        this.zbpid = zbpid;
    }

    public String getTbjzsj() {
        return tbjzsj;
    }

    public void setTbjzsj(String tbjzsj) {
        this.tbjzsj = tbjzsj;
    }

    public String getPbsj() {
        return pbsj;
    }

    public void setPbsj(String pbsj) {
        this.pbsj = pbsj;
    }

    public String getFbsj() {
        return fbsj;
    }

    public void setFbsj(String fbsj) {
        this.fbsj = fbsj;
    }

    public String getCjsj() {
        return cjsj;
    }

    public void setCjsj(String cjsj) {
        this.cjsj = cjsj;
    }

    public String getDbsj() {
        return dbsj;
    }

    public void setDbsj(String dbsj) {
        this.dbsj = dbsj;
    }
}