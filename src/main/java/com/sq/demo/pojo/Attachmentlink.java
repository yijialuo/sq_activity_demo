package com.sq.demo.pojo;

import javax.persistence.*;

public class Attachmentlink {
    /**
     * 附件id
     */
    private String attachment;

    /**
     * 用户id
     */
    @Column(name = "userId")
    private String userid;

    /**
     * 获取附件id
     *
     * @return attachment - 附件id
     */
    public String getAttachment() {
        return attachment;
    }

    /**
     * 设置附件id
     *
     * @param attachment 附件id
     */
    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    /**
     * 获取用户id
     *
     * @return userId - 用户id
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(String userid) {
        this.userid = userid;
    }
}