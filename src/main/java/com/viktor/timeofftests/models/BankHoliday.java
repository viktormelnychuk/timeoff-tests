package com.viktor.timeofftests.models;

import lombok.Data;

import java.util.Date;

@Data
public class BankHoliday {
    private Date date;
    private String name;

//    public BankHoliday(Date date, String name) {
//        this.date = date;
//        this.name = name;
//        System.out.println("test");
//    }
//
//    public BankHoliday (String date, String name){
//        System.out.println("test");
//    }
}
