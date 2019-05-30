package com.sq.demo.pojo;

import javax.persistence.*;

public class Yscjdwj {
    /**
     * id主键
     */
    @Id
    private String id;

    /**
     * 项目id
     */
    private String jlid;

    /**
     * 必传文件id
     */
    private String bcwjid;

    /**
     * 文件id
     */
    private String fid;

    /**
     * 文件名称
     */
    private String fname;

    /**
     * 上传人
     */
    private String scr;

    /**
     * 多余一
     */
    private String y1;

    /**
     * 多余二
     */
    private String y2;

    /**
     * 多余三
     */
    private String y3;

    /**
     * 获取id主键
     *
     * @return id - id主键
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id主键
     *
     * @param id id主键
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取项目id
     *
     * @return xmid - 项目id
     */
    public String getJlid() {
        return jlid;
    }

    /**
     * 设置项目id
     *
     * @param jlid 项目id
     */
    public void setJlid(String jlid) {
        this.jlid = jlid;
    }

    /**
     * 获取必传文件id
     *
     * @return bcwjid - 必传文件id
     */
    public String getBcwjid() {
        return bcwjid;
    }

    /**
     * 设置必传文件id
     *
     * @param bcwjid 必传文件id
     */
    public void setBcwjid(String bcwjid) {
        this.bcwjid = bcwjid;
    }

    /**
     * 获取文件id
     *
     * @return fid - 文件id
     */
    public String getFid() {
        return fid;
    }

    /**
     * 设置文件id
     *
     * @param fid 文件id
     */
    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 获取文件名称
     *
     * @return fname - 文件名称
     */
    public String getFname() {
        return fname;
    }

    /**
     * 设置文件名称
     *
     * @param fname 文件名称
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * 获取上传人
     *
     * @return scr - 上传人
     */
    public String getScr() {
        return scr;
    }

    /**
     * 设置上传人
     *
     * @param scr 上传人
     */
    public void setScr(String scr) {
        this.scr = scr;
    }

    /**
     * 获取多余一
     *
     * @return y1 - 多余一
     */
    public String getY1() {
        return y1;
    }

    /**
     * 设置多余一
     *
     * @param y1 多余一
     */
    public void setY1(String y1) {
        this.y1 = y1;
    }

    /**
     * 获取多余二
     *
     * @return y2 - 多余二
     */
    public String getY2() {
        return y2;
    }

    /**
     * 设置多余二
     *
     * @param y2 多余二
     */
    public void setY2(String y2) {
        this.y2 = y2;
    }

    /**
     * 获取多余三
     *
     * @return y3 - 多余三
     */
    public String getY3() {
        return y3;
    }

    /**
     * 设置多余三
     *
     * @param y3 多余三
     */
    public void setY3(String y3) {
        this.y3 = y3;
    }
}