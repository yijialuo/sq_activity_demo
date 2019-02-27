package com.sq.demo.pojo;

import javax.persistence.*;

public class Contractfile {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 合同id
     */
    @Column(name = "CID")
    private String cid;

    /**
     * 附件id
     */
    @Column(name = "FID")
    private String fid;

    /**
     * 附件名
     */
    @Column(name = "FNAME")
    private String fname;

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
     * 获取合同id
     *
     * @return CID - 合同id
     */
    public String getCid() {
        return cid;
    }

    /**
     * 设置合同id
     *
     * @param cid 合同id
     */
    public void setCid(String cid) {
        this.cid = cid;
    }

    /**
     * 获取附件id
     *
     * @return FID - 附件id
     */
    public String getFid() {
        return fid;
    }

    /**
     * 设置附件id
     *
     * @param fid 附件id
     */
    public void setFid(String fid) {
        this.fid = fid;
    }

    /**
     * 获取附件名
     *
     * @return FNAME - 附件名
     */
    public String getFname() {
        return fname;
    }

    /**
     * 设置附件名
     *
     * @param fname 附件名
     */
    public void setFname(String fname) {
        this.fname = fname;
    }
}