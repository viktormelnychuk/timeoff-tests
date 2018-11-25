package com.viktor.timeofftests.models;

import lombok.Data;

import java.sql.Date;
@Data
public class Session {
    private String sid;
    private String data;
    private Date expires;
    private Date createdAt;
    private Date updatedAt;

    public Session(String sid, String data, Date expires, Date createdAt, Date updatedAt) {
        this.sid = sid;
        this.data = data;
        this.expires = expires;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
