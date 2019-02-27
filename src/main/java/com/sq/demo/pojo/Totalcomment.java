package com.sq.demo.pojo;

import javax.persistence.*;

public class Totalcomment {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 供应商id
     */
    @Column(name = "SID")
    private String sid;

    /**
     * 承包范围
     */
    @Column(name = "CBFW")
    private String cbfw;

    /**
     * 承担项目情况 yy
     */
    @Column(name = "CDXMQK")
    private String cdxmqk;

    /**
     * 违章情况 yy
     */
    @Column(name = "WZQK")
    private String wzqk;

    /**
     * 评价年份 yy
     */
    @Column(name = "PJNF")
    private String pjnf;

    /**
     * 工程技术部总分 y
     */
    @Column(name = "GCJSBZF")
    private String gcjsbzf;

    /**
     * 工程技术部考评人 y
     */
    @Column(name = "GCJSBKPR")
    private String gcjsbkpr;

    /**
     * 粮油操作部总分 y
     */
    @Column(name = "LYCZBZF")
    private String lyczbzf;

    /**
     * 粮油操作部考评人 y
     */
    @Column(name = "LYCZBKPR")
    private String lyczbkpr;

    /**
     * 煤矿操作部总分 y
     */
    @Column(name = "MKCZBZF")
    private String mkczbzf;

    /**
     * 煤矿操作部考评人 y
     */
    @Column(name = "MKCZBKPR")
    private String mkczbkpr;

    /**
     * 项目委员会总分 y
     */
    @Column(name = "XMWYHZF")
    private String xmwyhzf;

    /**
     * 项目委员会考评人 y
     */
    @Column(name = "XMWYHKPR")
    private String xmwyhkpr;

    /**
     * 综合得分   y
     */
    @Column(name = "ZHDF")
    private String zhdf;

    /**
     * 工程技术部意见  y
     */
    @Column(name = "GCJSBYJ")
    private String gcjsbyj;

    /**
     * 项目管理委员会意见  y
     */
    @Column(name = "XMGLWYHYJ")
    private String xmglwyhyj;

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
     * 获取供应商id
     *
     * @return SID - 供应商id
     */
    public String getSid() {
        return sid;
    }

    /**
     * 设置供应商id
     *
     * @param sid 供应商id
     */
    public void setSid(String sid) {
        this.sid = sid;
    }

    /**
     * 获取承包范围
     *
     * @return CBFW - 承包范围
     */
    public String getCbfw() {
        return cbfw;
    }

    /**
     * 设置承包范围
     *
     * @param cbfw 承包范围
     */
    public void setCbfw(String cbfw) {
        this.cbfw = cbfw;
    }

    /**
     * 获取承担项目情况
     *
     * @return CDXMQK - 承担项目情况
     */
    public String getCdxmqk() {
        return cdxmqk;
    }

    /**
     * 设置承担项目情况
     *
     * @param cdxmqk 承担项目情况
     */
    public void setCdxmqk(String cdxmqk) {
        this.cdxmqk = cdxmqk;
    }

    /**
     * 获取违章情况
     *
     * @return WZQK - 违章情况
     */
    public String getWzqk() {
        return wzqk;
    }

    /**
     * 设置违章情况
     *
     * @param wzqk 违章情况
     */
    public void setWzqk(String wzqk) {
        this.wzqk = wzqk;
    }

    /**
     * 获取评价年份
     *
     * @return PJNF - 评价年份
     */
    public String getPjnf() {
        return pjnf;
    }

    /**
     * 设置评价年份
     *
     * @param pjnf 评价年份
     */
    public void setPjnf(String pjnf) {
        this.pjnf = pjnf;
    }

    /**
     * 获取工程技术部总分
     *
     * @return GCJSBZF - 工程技术部总分
     */
    public String getGcjsbzf() {
        return gcjsbzf;
    }

    /**
     * 设置工程技术部总分
     *
     * @param gcjsbzf 工程技术部总分
     */
    public void setGcjsbzf(String gcjsbzf) {
        this.gcjsbzf = gcjsbzf;
    }

    /**
     * 获取工程技术部考评人
     *
     * @return GCJSBKPR - 工程技术部考评人
     */
    public String getGcjsbkpr() {
        return gcjsbkpr;
    }

    /**
     * 设置工程技术部考评人
     *
     * @param gcjsbkpr 工程技术部考评人
     */
    public void setGcjsbkpr(String gcjsbkpr) {
        this.gcjsbkpr = gcjsbkpr;
    }

    /**
     * 获取粮油操作部总分
     *
     * @return LYCZBZF - 粮油操作部总分
     */
    public String getLyczbzf() {
        return lyczbzf;
    }

    /**
     * 设置粮油操作部总分
     *
     * @param lyczbzf 粮油操作部总分
     */
    public void setLyczbzf(String lyczbzf) {
        this.lyczbzf = lyczbzf;
    }

    /**
     * 获取粮油操作部考评人
     *
     * @return LYCZBKPR - 粮油操作部考评人
     */
    public String getLyczbkpr() {
        return lyczbkpr;
    }

    /**
     * 设置粮油操作部考评人
     *
     * @param lyczbkpr 粮油操作部考评人
     */
    public void setLyczbkpr(String lyczbkpr) {
        this.lyczbkpr = lyczbkpr;
    }

    /**
     * 获取煤矿操作部总分
     *
     * @return MKCZBZF - 煤矿操作部总分
     */
    public String getMkczbzf() {
        return mkczbzf;
    }

    /**
     * 设置煤矿操作部总分
     *
     * @param mkczbzf 煤矿操作部总分
     */
    public void setMkczbzf(String mkczbzf) {
        this.mkczbzf = mkczbzf;
    }

    /**
     * 获取煤矿操作部考评人
     *
     * @return MKCZBKPR - 煤矿操作部考评人
     */
    public String getMkczbkpr() {
        return mkczbkpr;
    }

    /**
     * 设置煤矿操作部考评人
     *
     * @param mkczbkpr 煤矿操作部考评人
     */
    public void setMkczbkpr(String mkczbkpr) {
        this.mkczbkpr = mkczbkpr;
    }

    /**
     * 获取项目委员会总分
     *
     * @return XMWYHZF - 项目委员会总分
     */
    public String getXmwyhzf() {
        return xmwyhzf;
    }

    /**
     * 设置项目委员会总分
     *
     * @param xmwyhzf 项目委员会总分
     */
    public void setXmwyhzf(String xmwyhzf) {
        this.xmwyhzf = xmwyhzf;
    }

    /**
     * 获取项目委员会考评人
     *
     * @return XMWYHKPR - 项目委员会考评人
     */
    public String getXmwyhkpr() {
        return xmwyhkpr;
    }

    /**
     * 设置项目委员会考评人
     *
     * @param xmwyhkpr 项目委员会考评人
     */
    public void setXmwyhkpr(String xmwyhkpr) {
        this.xmwyhkpr = xmwyhkpr;
    }

    /**
     * 获取综合得分
     *
     * @return ZHDF - 综合得分
     */
    public String getZhdf() {
        return zhdf;
    }

    /**
     * 设置综合得分
     *
     * @param zhdf 综合得分
     */
    public void setZhdf(String zhdf) {
        this.zhdf = zhdf;
    }

    /**
     * 获取工程技术部意见
     *
     * @return GCJSBYJ - 工程技术部意见
     */
    public String getGcjsbyj() {
        return gcjsbyj;
    }

    /**
     * 设置工程技术部意见
     *
     * @param gcjsbyj 工程技术部意见
     */
    public void setGcjsbyj(String gcjsbyj) {
        this.gcjsbyj = gcjsbyj;
    }

    /**
     * 获取项目管理委员会意见
     *
     * @return XMGLWYHYJ - 项目管理委员会意见
     */
    public String getXmglwyhyj() {
        return xmglwyhyj;
    }

    /**
     * 设置项目管理委员会意见
     *
     * @param xmglwyhyj 项目管理委员会意见
     */
    public void setXmglwyhyj(String xmglwyhyj) {
        this.xmglwyhyj = xmglwyhyj;
    }
}