package com.sq.demo.pojo;

import javax.persistence.*;

public class Supplier {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 供应商名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 供应商代码
     */
    @Column(name = "CODE")
    private String code;

    /**
     * 座机
     */
    @Column(name = "PHONE")
    private String phone;

    /**
     * 电话
     */
    @Column(name = "MOBILE")
    private String mobile;

    /**
     * 联系人
     */
    @Column(name = "CONTACT")
    private String contact;

    /**
     * 传真
     */
    @Column(name = "FAX")
    private String fax;

    /**
     * 邮箱
     */
    @Column(name = "EMAIL")
    private String email;

    /**
     * 地址
     */
    @Column(name = "ADDRESS")
    private String address;

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
     * 获取供应商名称
     *
     * @return NAME - 供应商名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置供应商名称
     *
     * @param name 供应商名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取供应商代码
     *
     * @return CODE - 供应商代码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置供应商代码
     *
     * @param code 供应商代码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取座机
     *
     * @return PHONE - 座机
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置座机
     *
     * @param phone 座机
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取电话
     *
     * @return MOBILE - 电话
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置电话
     *
     * @param mobile 电话
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取联系人
     *
     * @return CONTACT - 联系人
     */
    public String getContact() {
        return contact;
    }

    /**
     * 设置联系人
     *
     * @param contact 联系人
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * 获取传真
     *
     * @return FAX - 传真
     */
    public String getFax() {
        return fax;
    }

    /**
     * 设置传真
     *
     * @param fax 传真
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * 获取邮箱
     *
     * @return EMAIL - 邮箱
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置邮箱
     *
     * @param email 邮箱
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获取地址
     *
     * @return ADDRESS - 地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置地址
     *
     * @param address 地址
     */
    public void setAddress(String address) {
        this.address = address;
    }
}