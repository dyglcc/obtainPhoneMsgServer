package com.dx168.patchserver.core.domain;

import java.util.Date;
import java.util.List;

/**
 * `created_at`   DATETIME             DEFAULT NULL,
 * `updated_at`
 */
public class UserShareAppInfo {
    private int id;
    private String main_account;
    private List<AppShares> apps;
    private Date created_at;
    private Date updated_at;

    public String getMain_account() {
        return main_account;
    }

    public void setMain_account(String main_account) {
        this.main_account = main_account;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
