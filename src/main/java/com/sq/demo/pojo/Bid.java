package com.sq.demo.pojo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Bid {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 项目id
     */
    @Column(name = "PROJECT_ID")
    private String projectId;

    /**
     * 投标商
     */
    @Column(name = "BIDER")
    private String bider;

    /**
     * 金额
     */
    @Column(name = "AMOUNT")
    private BigDecimal amount;

    /**
     * 投标日期
     */
    @Column(name = "BID_DTE")
    private Date bidDte;

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
     * 获取项目id
     *
     * @return PROJECT_ID - 项目id
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * 设置项目id
     *
     * @param projectId 项目id
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取投标商
     *
     * @return BIDER - 投标商
     */
    public String getBider() {
        return bider;
    }

    /**
     * 设置投标商
     *
     * @param bider 投标商
     */
    public void setBider(String bider) {
        this.bider = bider;
    }

    /**
     * 获取金额
     *
     * @return AMOUNT - 金额
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取投标日期
     *
     * @return BID_DTE - 投标日期
     */
    public Date getBidDte() {
        return bidDte;
    }

    /**
     * 设置投标日期
     *
     * @param bidDte 投标日期
     */
    public void setBidDte(Date bidDte) {
        this.bidDte = bidDte;
    }
}