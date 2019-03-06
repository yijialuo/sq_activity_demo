package com.sq.demo.pojo;

import java.math.BigDecimal;
import javax.persistence.*;

public class Zhongbiao {
    /**
     * di
     */
    @Id
    private String id;

    /**
     * 招标id
     */
    private String zbid;

    /**
     * 项目id
     */
    private String xmid;

    /**
     * 中标单位
     */
    private String zhongbiaodw;

    /**
     * 中标价格
     */
    private BigDecimal zhongbiaojg;

    /**
     * 获取di
     *
     * @return id - di
     */
    public String getId() {
        return id;
    }

    /**
     * 设置di
     *
     * @param id di
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
     * 获取中标单位
     *
     * @return zhongbiaodw - 中标单位
     */
    public String getZhongbiaodw() {
        return zhongbiaodw;
    }

    /**
     * 设置中标单位
     *
     * @param zhongbiaodw 中标单位
     */
    public void setZhongbiaodw(String zhongbiaodw) {
        this.zhongbiaodw = zhongbiaodw;
    }

    /**
     * 获取中标价格
     *
     * @return zhongbiaojg - 中标价格
     */
    public BigDecimal getZhongbiaojg() {
        return zhongbiaojg;
    }

    /**
     * 设置中标价格
     *
     * @param zhongbiaojg 中标价格
     */
    public void setZhongbiaojg(BigDecimal zhongbiaojg) {
        this.zhongbiaojg = zhongbiaojg;
    }
}