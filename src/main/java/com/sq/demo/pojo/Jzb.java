package com.sq.demo.pojo;

import javax.persistence.*;

public class Jzb {
    /**
     * 机种名称
     */
    @Id
    private String jzmc;

    /**
     * 获取机种名称
     *
     * @return jzmc - 机种名称
     */
    public String getJzmc() {
        return jzmc;
    }

    /**
     * 设置机种名称
     *
     * @param jzmc 机种名称
     */
    public void setJzmc(String jzmc) {
        this.jzmc = jzmc;
    }
}