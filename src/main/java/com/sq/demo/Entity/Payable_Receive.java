package com.sq.demo.Entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by yijialuo on 2019/1/13.
 */
public class Payable_Receive implements Serializable {
    private String id;
    private String jbbm;
    private String jbr;
    private String yszmr;
    private String invoice_no;
    private String account;
    private String bank;
    private String project;
    private String contract_id;
    private BigDecimal bqyf;
    private String jbbmfzr;
    private String zgbmfzr;
    private String cwzj;
    private String fgld;
    private String gszjl;
    private String rq;


    public String getJbbm() {
        return jbbm;
    }

    public String getRq() {
        return rq;
    }

    public void setRq(String rq) {
        this.rq = rq;
    }

    public String getJbr() {
        return jbr;
    }

    public String getYszmr() {
        return yszmr;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public String getAccount() {
        return account;
    }

    public String getBank() {
        return bank;
    }

    public String getProject() {
        return project;
    }

    public String getContract_id() {
        return contract_id;
    }

    public BigDecimal getBqyf() {
        return bqyf;
    }

    public String getJbbmfzr() {
        return jbbmfzr;
    }

    public String getZgbmfzr() {
        return zgbmfzr;
    }

    public String getCwzj() {
        return cwzj;
    }

    public String getFgld() {
        return fgld;
    }

    public String getGszjl() {
        return gszjl;
    }

    public void setJbbm(String jbbm) {
        this.jbbm = jbbm;
    }

    public void setJbr(String jbr) {
        this.jbr = jbr;
    }

    public void setYszmr(String yszmr) {
        this.yszmr = yszmr;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public void setBqyf(BigDecimal bqyf) {
        this.bqyf = bqyf;
    }

    public void setJbbmfzr(String jbbmfzr) {
        this.jbbmfzr = jbbmfzr;
    }

    public void setZgbmfzr(String zgbmfzr) {
        this.zgbmfzr = zgbmfzr;
    }

    public void setCwzj(String cwzj) {
        this.cwzj = cwzj;
    }

    public void setFgld(String fgld) {
        this.fgld = fgld;
    }

    public void setGszjl(String gszjl) {
        this.gszjl = gszjl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
