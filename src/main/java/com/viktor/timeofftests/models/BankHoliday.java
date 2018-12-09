package com.viktor.timeofftests.models;

import lombok.Data;

import java.util.Date;

@Data
public class BankHoliday {
    private Date date;
    private String name;
    private int id;
    private int companyId;
}
