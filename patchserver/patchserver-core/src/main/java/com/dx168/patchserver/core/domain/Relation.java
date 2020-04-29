package com.dx168.patchserver.core.domain;

import java.util.Date;

/**
 * /**
 * `id`           INT         NOT NULL AUTO_INCREMENT,
 * `user_phone`      VARCHAR(32)               DEFAULT NULL,
 * `relate_phone`      VARCHAR(32)               DEFAULT NULL,
 * `created_at`   DATETIME             DEFAULT NULL,
 * `updated_at`   DATETIME
 */
public class Relation {
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getRelate_phone() {
        return relate_phone;
    }

    public void setRelate_phone(String relate_phone) {
        this.relate_phone = relate_phone;
    }

    private String user_phone;


    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    private int group_id;
    private String relate_phone;
    private Date updated_at;
    private Date created_at;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
