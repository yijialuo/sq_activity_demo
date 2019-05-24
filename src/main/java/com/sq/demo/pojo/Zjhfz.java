package com.sq.demo.pojo;

import javax.persistence.*;

public class Zjhfz {
    /**
     * 阈值
     */
    @Id
    private String id;

    private String fz;

    /**
     * 获取阈值
     *
     * @return fz - 阈值
     */
    public String getFz() {
        return fz;
    }

    /**
     * 设置阈值
     *
     * @param fz 阈值
     */
    public void setFz(String fz) {
        this.fz = fz;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}