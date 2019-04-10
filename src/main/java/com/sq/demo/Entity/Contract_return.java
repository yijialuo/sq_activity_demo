package com.sq.demo.Entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yijialuo on 2019/1/13.
 */
public class Contract_return implements Serializable {

    private String id;
    private String contractNo;
    private String projectName;
    private String projectId;
    private String xmNo;
    private String dfdsr;
    private String tzwh;
    private BigDecimal price;
    private String jbr;
    private String zbdwyj;
    private String zzsc;
    private String psjl;
    private String dwyj;
    private String cwbmyj;
    private String fgldyj;
    private String zjlyj;
    private String fjid;
    private String rq;
    private String gd;

    public String getId() {
        return id;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getDfdsr() {
        return dfdsr;
    }

    public String getTzwh() {
        return tzwh;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getJbr() {
        return jbr;
    }

    public String getZbdwyj() {
        return zbdwyj;
    }

    public String getZzsc() {
        return zzsc;
    }

    public String getPsjl() {
        return psjl;
    }

    public String getDwyj() {
        return dwyj;
    }

    public String getCwbmyj() {
        return cwbmyj;
    }

    public String getFgldyj() {
        return fgldyj;
    }

    public String getZjlyj() {
        return zjlyj;
    }

    public String getFjid() {
        return fjid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public void setDfdsr(String dfdsr) {
        this.dfdsr = dfdsr;
    }

    public void setTzwh(String tzwh) {
        this.tzwh = tzwh;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setJbr(String jbr) {
        this.jbr = jbr;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setZbdwyj(String zbdwyj) {
        this.zbdwyj = zbdwyj;
    }

    public void setZzsc(String zzsc) {
        this.zzsc = zzsc;
    }

    public void setPsjl(String psjl) {
        this.psjl = psjl;
    }

    public void setDwyj(String dwyj) {
        this.dwyj = dwyj;
    }

    public void setCwbmyj(String cwbmyj) {
        this.cwbmyj = cwbmyj;
    }

    public void setFgldyj(String fgldyj) {
        this.fgldyj = fgldyj;
    }

    public void setZjlyj(String zjlyj) {
        this.zjlyj = zjlyj;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getGd() {
        return gd;
    }

    public void setGd(String gd) {
        this.gd = gd;
    }

    public String getXmNo() {
        return xmNo;
    }

    public void setXmNo(String xmNo) {
        this.xmNo = xmNo;
    }
}
