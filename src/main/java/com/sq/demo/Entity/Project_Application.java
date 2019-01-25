package com.sq.demo.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yijialuo on 2019/1/13.
 */
public class Project_Application implements Serializable {
    private String pi;//流程实例Id
    private String userId;
    private String project_id;
    private String project_type;
    private String project_no;
    private String project_name;
    private String investment_establish;
    private String person_in_charge;
    private String establish_reason;
    private String scale;
    private String finish_dte;
    private String declaration_dep;
    private String illustration;
    private String proposer;
    private String application_dte;
    private String reviser;
    private String revise_dte;

    public String getPi() {
        return pi;
    }

    public String getUserId() {
        return userId;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getProject_type() {
        return project_type;
    }

    public String getProject_no() {
        return project_no;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getInvestment_establish() {
        return investment_establish;
    }

    public String getPerson_in_charge() {
        return person_in_charge;
    }

    public String getEstablish_reason() {
        return establish_reason;
    }

    public String getScale() {
        return scale;
    }

    public String getFinish_dte() {
        return finish_dte;
    }

    public String getDeclaration_dep() {
        return declaration_dep;
    }

    public String getIllustration() {
        return illustration;
    }

    public String getProposer() {
        return proposer;
    }

    public String getApplication_dte() {
        return application_dte;
    }

    public String getReviser() {
        return reviser;
    }

    public String getRevise_dte() {
        return revise_dte;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
    }

    public void setProject_no(String project_no) {
        this.project_no = project_no;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public void setInvestment_establish(String investment_establish) {
        this.investment_establish = investment_establish;
    }

    public void setPerson_in_charge(String person_in_charge) {
        this.person_in_charge = person_in_charge;
    }

    public void setEstablish_reason(String establish_reason) {
        this.establish_reason = establish_reason;
    }

    public void setScale(String scale) {
        this.scale = scale;
    }

    public void setFinish_dte(String finish_dte) {
        this.finish_dte = finish_dte;
    }

    public void setDeclaration_dep(String declaration_dep) {
        this.declaration_dep = declaration_dep;
    }

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }

    public void setProposer(String proposer) {
        this.proposer = proposer;
    }

    public void setApplication_dte(String application_dte) {
        this.application_dte = application_dte;
    }

    public void setReviser(String reviser) {
        this.reviser = reviser;
    }

    public void setRevise_dte(String revise_dte) {
        this.revise_dte = revise_dte;
    }
}
