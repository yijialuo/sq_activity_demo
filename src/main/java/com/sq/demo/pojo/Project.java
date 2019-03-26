package com.sq.demo.pojo;


import javax.persistence.*;
import java.io.Serializable;


public class Project implements Serializable {
    /**
     * 主键
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 编号
     */
    @Column(name = "PROJECT_NO")
    private String projectNo;

    /**
     * 项目类型
     */
    @Column(name = "PROJECT_TYPE")
    private String projectType;

    /**
     * 项目名称
     */
    @Column(name = "PROJECT_NAM")
    private String projectNam;

    /**
     * 申报部门
     */
    @Column(name = "DECLARATION_DEP")
    private String declarationDep;

    /**
     * 负责人
     */
    @Column(name = "PERSON_IN_CHARGE")
    private String personInCharge;

    /**
     * 投资概算
     */
    @Column(name = "INVESTMENT_ESTIMATE")
    private String investmentEstimate;

    /**
     * 立项理由
     */
    @Column(name = "ESTABLISH_REASON")
    private String establishReason;

    /**
     * 内容规模
     */
    @Column(name = "SCALE")
    private String scale;

    /**
     * 部门审核意见(项目大类)
     */
    @Column(name = "DEP_AUDIT_OPINION")
    private String depAuditOpinion;

    /**
     * 主管部门技术审核意见（预计工期）
     */
    @Column(name = "TECH_AUDIT_OPINION")
    private String techAuditOpinion;

    /**
     * 工程技术部审核意见 创建时间
     */
    @Column(name = "ENG_TECH_AUDIT_OPINION")
    private String engTechAuditOpinion;

    /**
     * 中标单位(是否删除  1为删除)
     */
    @Column(name = "BIDER")
    private String bider;

    /**
     * 完成时间
     */
    @Column(name = "FINISH_DTE")
    private String finishDte;

    /**
     * 申请人
     */
    @Column(name = "PROPOSER")
    private String proposer;

    /**
     * 申请时间
     */
    @Column(name = "APPLICATION_DTE")
    private String applicationDte;

    /**
     * 修改人 现在改为项目分类
     */
    @Column(name = "REVISER")
    private String reviser;

    /**
     * 修改时间
     */
    @Column(name = "REVISE_DTE")
    private String reviseDte;

    /**
     * 说明
     */
    @Column(name = "ILLUSTRATION")
    private String illustration;

    @Column(name = "PID")
    private String pid;

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
     * 获取编号
     *
     * @return PROJECT_NO - 编号
     */
    public String getProjectNo() {
        return projectNo;
    }

    /**
     * 设置编号
     *
     * @param projectNo 编号
     */
    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    /**
     * 获取项目类型
     *
     * @return PROJECT_TYPE - 项目类型
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * 设置项目类型
     *
     * @param projectType 项目类型
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**
     * 获取项目名称
     *
     * @return PROJECT_NAM - 项目名称
     */
    public String getProjectNam() {
        return projectNam;
    }

    /**
     * 设置项目名称
     *
     * @param projectNam 项目名称
     */
    public void setProjectNam(String projectNam) {
        this.projectNam = projectNam;
    }

    /**
     * 获取申报部门
     *
     * @return DECLARATION_DEP - 申报部门
     */
    public String getDeclarationDep() {
        return declarationDep;
    }

    /**
     * 设置申报部门
     *
     * @param declarationDep 申报部门
     */
    public void setDeclarationDep(String declarationDep) {
        this.declarationDep = declarationDep;
    }

    /**
     * 获取负责人
     *
     * @return PERSON_IN_CHARGE - 负责人
     */
    public String getPersonInCharge() {
        return personInCharge;
    }

    /**
     * 设置负责人
     *
     * @param personInCharge 负责人
     */
    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    /**
     * 获取投资概算
     *
     * @return INVESTMENT_ESTIMATE - 投资概算
     */
    public String getInvestmentEstimate() {
        return investmentEstimate;
    }

    /**
     * 设置投资概算
     *
     * @param investmentEstimate 投资概算
     */
    public void setInvestmentEstimate(String investmentEstimate) {
        this.investmentEstimate = investmentEstimate;
    }

    /**
     * 获取立项理由
     *
     * @return ESTABLISH_REASON - 立项理由
     */
    public String getEstablishReason() {
        return establishReason;
    }

    /**
     * 设置立项理由
     *
     * @param establishReason 立项理由
     */
    public void setEstablishReason(String establishReason) {
        this.establishReason = establishReason;
    }

    /**
     * 获取内容规模
     *
     * @return SCALE - 内容规模
     */
    public String getScale() {
        return scale;
    }

    /**
     * 设置内容规模
     *
     * @param scale 内容规模
     */
    public void setScale(String scale) {
        this.scale = scale;
    }

    /**
     * 获取部门审核意见
     *
     * @return DEP_AUDIT_OPINION - 部门审核意见
     */
    public String getDepAuditOpinion() {
        return depAuditOpinion;
    }

    /**
     * 设置部门审核意见
     *
     * @param depAuditOpinion 部门审核意见
     */
    public void setDepAuditOpinion(String depAuditOpinion) {
        this.depAuditOpinion = depAuditOpinion;
    }

    /**
     * 获取主管部门技术审核意见
     *
     * @return TECH_AUDIT_OPINION - 主管部门技术审核意见
     */
    public String getTechAuditOpinion() {
        return techAuditOpinion;
    }

    /**
     * 设置主管部门技术审核意见
     *
     * @param techAuditOpinion 主管部门技术审核意见
     */
    public void setTechAuditOpinion(String techAuditOpinion) {
        this.techAuditOpinion = techAuditOpinion;
    }

    /**
     * 获取工程技术部审核意见
     *
     * @return ENG_TECH_AUDIT_OPINION - 工程技术部审核意见
     */
    public String getEngTechAuditOpinion() {
        return engTechAuditOpinion;
    }

    /**
     * 设置工程技术部审核意见
     *
     * @param engTechAuditOpinion 工程技术部审核意见
     */
    public void setEngTechAuditOpinion(String engTechAuditOpinion) {
        this.engTechAuditOpinion = engTechAuditOpinion;
    }

    /**
     * 获取中标单位
     *
     * @return BIDER - 中标单位
     */
    public String getBider() {
        return bider;
    }

    /**
     * 设置中标单位
     *
     * @param bider 中标单位
     */
    public void setBider(String bider) {
        this.bider = bider;
    }

    /**
     * 获取完成时间
     *
     * @return FINISH_DTE - 完成时间
     */
    public String getFinishDte() {
        return finishDte;
    }

    /**
     * 设置完成时间
     *
     * @param finishDte 完成时间
     */
    public void setFinishDte(String finishDte) {
        this.finishDte = finishDte;
    }

    /**
     * 获取申请人
     *
     * @return PROPOSER - 申请人
     */
    public String getProposer() {
        return proposer;
    }

    /**
     * 设置申请人
     *
     * @param proposer 申请人
     */
    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    /**
     * 获取申请时间
     *
     * @return APPLICATION_DTE - 申请时间
     */
    public String getApplicationDte() {
        return applicationDte;
    }

    /**
     * 设置申请时间
     *
     * @param applicationDte 申请时间
     */
    public void setApplicationDte(String applicationDte) {
        this.applicationDte = applicationDte;
    }

    /**
     * 获取修改人
     *
     * @return REVISER - 修改人
     */
    public String getReviser() {
        return reviser;
    }

    /**
     * 设置修改人
     *
     * @param reviser 修改人
     */
    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    /**
     * 获取修改时间
     *
     * @return REVISE_DTE - 修改时间
     */
    public String getReviseDte() {
        return reviseDte;
    }

    /**
     * 设置修改时间
     *
     * @param reviseDte 修改时间
     */
    public void setReviseDte(String reviseDte) {
        this.reviseDte = reviseDte;
    }

    /**
     * 获取说明
     *
     * @return ILLUSTRATION - 说明
     */
    public String getIllustration() {
        return illustration;
    }

    /**
     * 设置说明
     *
     * @param illustration 说明
     */
    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    /**
     * @return PID
     */
    public String getPid() {
        return pid;
    }

    /**
     * @param pid
     */
    public void setPid(String pid) {
        this.pid = pid;
    }
}