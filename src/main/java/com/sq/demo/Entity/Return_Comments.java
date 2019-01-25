package com.sq.demo.Entity;

import java.io.Serializable;

/**
 * Created by yijialuo on 2019/1/13.
 */
public class Return_Comments implements Serializable {
    private String usernam;
    private String comment;
    private String time;

    public String getUsernam() {
        return usernam;
    }

    public String getComment() {
        return comment;
    }

    public void setUsernam(String usernam) {
        this.usernam = usernam;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
