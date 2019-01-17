package com.sq.demo.pojo;

import javax.persistence.*;

public class Department {
    @Column(name = "D_COD")
    private String dCod;

    @Column(name = "D_NAM")
    private String dNam;

    @Id
    @Column(name = "ID")
    private String id;

    /**
     * @return D_COD
     */
    public String getdCod() {
        return dCod;
    }

    /**
     * @param dCod
     */
    public void setdCod(String dCod) {
        this.dCod = dCod;
    }

    /**
     * @return D_NAM
     */
    public String getdNam() {
        return dNam;
    }

    /**
     * @param dNam
     */
    public void setdNam(String dNam) {
        this.dNam = dNam;
    }

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }
}