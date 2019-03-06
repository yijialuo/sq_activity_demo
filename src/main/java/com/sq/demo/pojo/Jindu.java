package com.sq.demo.pojo;

import javax.persistence.*;

public class Jindu {
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
     * 施工节点
     */
    @Column(name = "POINT")
    private String point;

    /**
     * 时间节点
     */
    @Column(name = "FINISH_DATE")
    private String finishDate;

    /**
     * 完成情况
     */
    @Column(name = "IS_FINISH")
    private String isFinish;

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
     * 获取施工节点
     *
     * @return POINT - 施工节点
     */
    public String getPoint() {
        return point;
    }

    /**
     * 设置施工节点
     *
     * @param point 施工节点
     */
    public void setPoint(String point) {
        this.point = point;
    }

    /**
     * 获取时间节点
     *
     * @return FINISH_DATE - 时间节点
     */
    public String getFinishDate() {
        return finishDate;
    }

    /**
     * 设置时间节点
     *
     * @param finishDate 时间节点
     */
    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    /**
     * 获取完成情况
     *
     * @return IS_FINISH - 完成情况
     */
    public String getIsFinish() {
        return isFinish;
    }

    /**
     * 设置完成情况
     *
     * @param isFinish 完成情况
     */
    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }
}