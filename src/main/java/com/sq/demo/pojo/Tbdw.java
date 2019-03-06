package com.sq.demo.pojo;

import javax.persistence.*;

public class Tbdw {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 招标id
     */
    private String zbid;

    /**
     * 投标单位
     */
    private String dw;

    /**
     * 获取id
     *
     * @return id - id
     */
    public String getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取招标id
     *
     * @return zbid - 招标id
     */
    public String getZbid() {
        return zbid;
    }

    /**
     * 设置招标id
     *
     * @param zbid 招标id
     */
    public void setZbid(String zbid) {
        this.zbid = zbid;
    }



    public String getDw() {
        return dw;
    }

    public void setDw(String dw) {
        this.dw = dw;
    }
}