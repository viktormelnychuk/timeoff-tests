package com.viktor.timeofftests.models;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
@Data
public class Schedule {
    private int id;
    private int monday = 1;
    private int tuesday = 1;
    private int wednesday = 1;
    private int thursday = 1;
    private int friday = 1;
    private int saturday = 2;
    private int sunday = 2;
    private Timestamp createdAt = new Timestamp(new Date().getTime());
    private Timestamp updatedAt = new Timestamp(new Date().getTime());
    private Integer companyId;
    private Integer userID;

    public Schedule(int id, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, Timestamp createdAt, int companyId, int userID) {
        this.id = id;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.createdAt = createdAt;
        this.companyId = companyId;
        this.userID = userID;
    }
    public Schedule (int companyId){
        this.companyId = companyId;
    }
    public Schedule(){}
}
