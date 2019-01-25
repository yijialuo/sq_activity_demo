package com.sq.demo.Entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yijialuo on 2019/1/13.
 */
public class Project_Receive implements Serializable {
    private String userId;
    private String project_type;
    private String project_name;
    private String investment_establish;
    private String person_in_charge;
    private String establish_reason;
    private String scale;
    private String illustration;

    public String getUserId() {
        return userId;
    }

    public String getProject_type() {
        return project_type;
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

    public String getIllustration() {
        return illustration;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setProject_type(String project_type) {
        this.project_type = project_type;
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

    public void setIllustration(String illustration) {
        this.illustration = illustration;
    }
}
