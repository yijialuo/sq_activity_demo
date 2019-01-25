package com.sq.demo.pojo;

import javax.persistence.*;

@Table(name = "contract_file")
public class ContractFile {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 路径
     */
    @Column(name = "URL")
    private String url;

    /**
     * 合同号
     */
    @Column(name = "CONTRACT_ID")
    private String contractId;

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
     * 获取路径
     *
     * @return URL - 路径
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置路径
     *
     * @param url 路径
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取合同号
     *
     * @return CONTRACT_ID - 合同号
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * 设置合同号
     *
     * @param contractId 合同号
     */
    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}