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
    private Integer companyId;
    private Integer userID;

    public Schedule(int id, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, int companyId, int userID) {
        this.id = id;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.companyId = companyId;
        this.userID = userID;
    }
    public Schedule (int companyId){
        this.companyId = companyId;
    }
    public Schedule(){}

    public void set (String day, boolean checked){
        switch (day.toLowerCase()){
            case "monday":
                this.setMonday(getFromBool(checked));
                break;
            case "tuesday":
                this.setTuesday(getFromBool(checked));
                break;
            case "wednesday":
                this.setWednesday(getFromBool(checked));
                break;
            case "thursday":
                this.setThursday(getFromBool(checked));
                break;
            case "friday":
                this.setFriday(getFromBool(checked));
                break;
            case "saturday":
                this.setSaturday(getFromBool(checked));
                break;
            case "sunday":
                this.setSunday(getFromBool(checked));
                break;
        }
    }

    private int getFromBool(boolean b){
        if(b){
            return 1;
        } else {
            return 2;
        }
    }
}
