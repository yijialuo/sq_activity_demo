package com.sq.demo.Entity;

import java.io.Serializable;

/**
 * Created by yijialuo on 2019/1/13.
 */
public class AttachmentReturn implements Serializable {
    private String attachment_nam;
    private String attachment_id;
    private String scr;
    private String scsj;

    public String getAttachment_nam() {
        return attachment_nam;
    }

    public String getAttachment_id() {
        return attachment_id;
    }

    public void setAttachment_nam(String attachment_nam) {
        this.attachment_nam = attachment_nam;
    }

    public void setAttachment_id(String attachment_id) {
        this.attachment_id = attachment_id;
    }

    public String getScr() {
        return scr;
    }

    public void setScr(String scr) {
        this.scr = scr;
    }

    public String getScsj() {
        return scsj;
    }

    public void setScsj(String scsj) {
        this.scsj = scsj;
    }
}
