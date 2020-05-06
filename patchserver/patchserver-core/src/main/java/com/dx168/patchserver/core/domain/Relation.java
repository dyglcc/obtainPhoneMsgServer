package com.dx168.patchserver.core.domain;

import java.util.Date;

/**
 * /**
 * `id`           INT         NOT NULL AUTO_INCREMENT,
 * `main_account`      VARCHAR(32)               DEFAULT NULL,
 * `sub_account`      VARCHAR(32)               DEFAULT NULL,
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


    public String getMain_account() {
        return main_account;
    }

    public void setMain_account(String main_account) {
        this.main_account = main_account;
    }

    public String getSub_account() {
        return sub_account;
    }

    public void setSub_account(String sub_account) {
        this.sub_account = sub_account;
    }

    private String main_account;


    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    private int group_id;
    private String sub_account;
    private Date updated_at;
    private Date created_at;

    // share app
    private String icon_url;

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getApp_name() {
        return app_name;
    }

    public void setApp_name(String app_name) {
        this.app_name = app_name;
    }

    private String app_name;


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
