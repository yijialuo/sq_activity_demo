package com.sq.demo.pojo;

import java.util.Date;
import javax.persistence.*;

public class Contract {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 合同号
     */
    @Column(name = "CONTRACT_NO")
    private String contractNo;

    /**
     * 合同名称
     */
    @Column(name = "CONTRACT_NAM")
    private String contractNam;

    /**
     * 合同类型
     */
    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    /**
     * 签署人
     */
    @Column(name = "SIGN_NAM")
    private String signNam;

    /**
     * 合同状态
     */
    @Column(name = "CONTRACT_STATUS")
    private String contractStatus;

    /**
     * 有效期
     */
    @Column(name = "VALID_DTE")
    private Date validDte;

    /**
     * 合同种类（收款付款）
     */
    @Column(name = "CONTRACT_PURPOSE")
    private String contractPurpose;

    /**
     * 作废标志
     */
    @Column(name = "IS_DEL")
    private Byte isDel;

    /**
     * 使用标志
     */
    @Column(name = "IS_USED")
    private Byte isUsed;

    /**
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 签署时间
     */
    @Column(name = "SIGN_DTE")
    private Date signDte;

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
     * 获取合同号
     *
     * @return CONTRACT_NO - 合同号
     */
    public String getContractNo() {
        return contractNo;
    }

    /**
     * 设置合同号
     *
     * @param contractNo 合同号
     */
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    /**
     * 获取合同名称
     *
     * @return CONTRACT_NAM - 合同名称
     */
    public String getContractNam() {
        return contractNam;
    }

    /**
     * 设置合同名称
     *
     * @param contractNam 合同名称
     */
    public void setContractNam(String contractNam) {
        this.contractNam = contractNam;
    }

    /**
     * 获取合同类型
     *
     * @return CONTRACT_TYPE - 合同类型
     */
    public String getContractType() {
        return contractType;
    }

    /**
     * 设置合同类型
     *
     * @param contractType 合同类型
     */
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    /**
     * 获取签署人
     *
     * @return SIGN_NAM - 签署人
     */
    public String getSignNam() {
        return signNam;
    }

    /**
     * 设置签署人
     *
     * @param signNam 签署人
     */
    public void setSignNam(String signNam) {
        this.signNam = signNam;
    }

    /**
     * 获取合同状态
     *
     * @return CONTRACT_STATUS - 合同状态
     */
    public String getContractStatus() {
        return contractStatus;
    }

    /**
     * 设置合同状态
     *
     * @param contractStatus 合同状态
     */
    public void setContractStatus(String contractStatus) {
        this.contractStatus = contractStatus;
    }

    /**
     * 获取有效期
     *
     * @return VALID_DTE - 有效期
     */
    public Date getValidDte() {
        return validDte;
    }

    /**
     * 设置有效期
     *
     * @param validDte 有效期
     */
    public void setValidDte(Date validDte) {
        this.validDte = validDte;
    }

    /**
     * 获取合同种类（收款付款）
     *
     * @return CONTRACT_PURPOSE - 合同种类（收款付款）
     */
    public String getContractPurpose() {
        return contractPurpose;
    }

    /**
     * 设置合同种类（收款付款）
     *
     * @param contractPurpose 合同种类（收款付款）
     */
    public void setContractPurpose(String contractPurpose) {
        this.contractPurpose = contractPurpose;
    }

    /**
     * 获取作废标志
     *
     * @return IS_DEL - 作废标志
     */
    public Byte getIsDel() {
        return isDel;
    }

    /**
     * 设置作废标志
     *
     * @param isDel 作废标志
     */
    public void setIsDel(Byte isDel) {
        this.isDel = isDel;
    }

    /**
     * 获取使用标志
     *
     * @return IS_USED - 使用标志
     */
    public Byte getIsUsed() {
        return isUsed;
    }

    /**
     * 设置使用标志
     *
     * @param isUsed 使用标志
     */
    public void setIsUsed(Byte isUsed) {
        this.isUsed = isUsed;
    }

    /**
     * 获取备注
     *
     * @return REMARK - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取签署时间
     *
     * @return SIGN_DTE - 签署时间
     */
    public Date getSignDte() {
        return signDte;
    }

    /**
     * 设置签署时间
     *
     * @param signDte 签署时间
     */
    public void setSignDte(Date signDte) {
        this.signDte = signDte;
    }
}