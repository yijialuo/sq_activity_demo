package com.sq.demo.pojo;

import javax.persistence.*;

@Table(name = "dep_rank")
public class DepRank {
    @Column(name = "DEPARTMENT_NAM")
    private String departmentNam;

    @Column(name = "RANK")
    private String rank;

    /**
     * @return DEPARTMENT_NAM
     */
    public String getDepartmentNam() {
        return departmentNam;
    }

    /**
     * @param departmentNam
     */
    public void setDepartmentNam(String departmentNam) {
        this.departmentNam = departmentNam;
    }

    /**
     * @return RANK
     */
    public String getRank() {
        return rank;
    }

    /**
     * @param rank
     */
    public void setRank(String rank) {
        this.rank = rank;
    }
}