package com.sq.demo.pojo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

public class Xxmcb {
    /**
     * id
     */
    @Id
    private String id;

    /**
     * 小项目id
     */
    private String xxmid;

    /**
     * 序号
     */
    private String xh;

    private String xxmbh;

    /**
     * 工作步骤名称
     */
    private String gzbzmc;

    /**
     * 施工单位
     */
    private String sgdw;

    /**
     * 合同金额
     */
    private BigDecimal htje;

    /**
     * 合同签订时间
     */
    private String htqdsj;

    /**
     * 备注
     */
    private String bz;

    /**
     * 创建时间
     */
    private String cjsj;

    /**
     * 修改时间
     */
    private Date xgsj;

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
     * 获取小项目id
     *
     * @return xxmid - 小项目id
     */
    public String getXxmid() {
        return xxmid;
    }

    /**
     * 设置小项目id
     *
     * @param xxmid 小项目id
     */
    public void setXxmid(String xxmid) {
        this.xxmid = xxmid;
    }

    /**
     * 获取序号
     *
     * @return xh - 序号
     */
    public String getXh() {
        return xh;
    }

    /**
     * 设置序号
     *
     * @param xh 序号
     */
    public void setXh(String xh) {
        this.xh = xh;
    }

    /**
     * 获取工作步骤名称
     *
     * @return gzbzmc - 工作步骤名称
     */
    public String getGzbzmc() {
        return gzbzmc;
    }

    /**
     * 设置工作步骤名称
     *
     * @param gzbzmc 工作步骤名称
     */
    public void setGzbzmc(String gzbzmc) {
        this.gzbzmc = gzbzmc;
    }

    /**
     * 获取施工单位
     *
     * @return sgdw - 施工单位
     */
    public String getSgdw() {
        return sgdw;
    }

    /**
     * 设置施工单位
     *
     * @param sgdw 施工单位
     */
    public void setSgdw(String sgdw) {
        this.sgdw = sgdw;
    }

    /**
     * 获取合同金额
     *
     * @return htje - 合同金额
     */
    public BigDecimal getHtje() {
        return htje;
    }

    /**
     * 设置合同金额
     *
     * @param htje 合同金额
     */
    public void setHtje(BigDecimal htje) {
        this.htje = htje;
    }

    /**
     * 获取合同签订时间
     *
     * @return htqdsj - 合同签订时间
     */
    public String getHtqdsj() {
        return htqdsj;
    }

    /**
     * 设置合同签订时间
     *
     * @param htqdsj 合同签订时间
     */
    public void setHtqdsj(String htqdsj) {
        this.htqdsj = htqdsj;
    }

    /**
     * 获取备注
     *
     * @return bz - 备注
     */
    public String getBz() {
        return bz;
    }

    /**
     * 设置备注
     *
     * @param bz 备注
     */
    public void setBz(String bz) {
        this.bz = bz;
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
     * 获取修改时间
     *
     * @return xgsj - 修改时间
     */
    public Date getXgsj() {
        return xgsj;
    }

    /**
     * 设置修改时间
     *
     * @param xgsj 修改时间
     */
    public void setXgsj(Date xgsj) {
        this.xgsj = xgsj;
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

    public String getXxmbh() {
        return xxmbh;
    }

    public void setXxmbh(String xxmbh) {
        this.xxmbh = xxmbh;
    }
}