package com.dx168.patchserver.core.domain;

import java.util.Date;

/**
 * `id`           INT         NOT NULL AUTO_INCREMENT,
 * `server_location`      VARCHAR(32)               DEFAULT NULL,
 * `port`     int           DEFAULT 1,
 * `add`      VARCHAR(32)               DEFAULT NULL,
 * `ratio`    VARCHAR(10)          DEFAULT NULL,
 * `created_at`   DATETIME             DEFAULT NULL,
 * `updated_at`
 */
public class Server {
    private int id;
    private String server_location;
    private int port;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String address;

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }

    private String ratio;
    private Date created_at;
    private Date updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServer_location() {
        return server_location;
    }

    public void setServer_location(String server_location) {
        this.server_location = server_location;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
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
