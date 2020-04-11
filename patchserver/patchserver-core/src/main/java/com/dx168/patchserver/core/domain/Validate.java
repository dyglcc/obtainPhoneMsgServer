package com.dx168.patchserver.core.domain;

import java.util.Date;

/**
 * `id` int(11) NOT NULL AUTO_INCREMENT,
 * `user_id` int(11) NOT NULL,
 * `email` varchar(40) NOT NULL,
 * `reset_token` varchar(40) NOT NULL,
 * `type` varchar(20) NOT NULL,
 * `gmt_create` datetime DEFAULT NULL,
 * `gmt_modified` datetime DEFAULT NULL,
 */
public class Validate {
    private Integer id;
    private Integer userid;
    private String email;
    private String resetToken;

    private String type;
    private Date gmtCreate;

    private Date gmtUpdate;
    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }



}
