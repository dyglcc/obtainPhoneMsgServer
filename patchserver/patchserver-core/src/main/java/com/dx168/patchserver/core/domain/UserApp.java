package com.dx168.patchserver.core.domain;

import java.util.Date;
import java.util.List;

/**
 * `created_at`   DATETIME             DEFAULT NULL,
 * `updated_at`
 */
public class UserApp {
    private int id;

    public int getApp_id() {
        return app_id;
    }

    public void setApp_id(int app_id) {
        this.app_id = app_id;
    }

    private int app_id;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    private String main_account;
    private int status;
    private AppShares app;
    private Date created_at;

    public AppShares getApp() {
        return app;
    }

    public void setApp(AppShares app) {
        this.app = app;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    private Date updated_at;
    private List<Relation> relations;

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
