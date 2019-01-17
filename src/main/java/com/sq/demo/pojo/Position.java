package com.sq.demo.pojo;

import javax.persistence.*;

public class Position {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "P_NAM")
    private String pNam;

    @Column(name = "P_COD")
    private String pCod;

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

    /**
     * @return P_NAM
     */
    public String getpNam() {
        return pNam;
    }

    /**
     * @param pNam
     */
    public void setpNam(String pNam) {
        this.pNam = pNam;
    }

    /**
     * @return P_COD
     */
    public String getpCod() {
        return pCod;
    }

    /**
     * @param pCod
     */
    public void setpCod(String pCod) {
        this.pCod = pCod;
    }
}