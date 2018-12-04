package com.viktor.timeofftests.models;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LeaveType {
    private int id;
    private String name;
    private String colorHex;
    private boolean useAllowance;
    private int limit;
    private int sortOrder;
    private int companyId;

    public LeaveType(String name, String colorHex, boolean useAllowance, int limit, int sortOrder, int companyId){
        this.name = name;
        this.colorHex = colorHex;
        this.useAllowance = useAllowance;
        this.limit = limit;
        this.sortOrder = sortOrder;
        this.companyId = companyId;
    }

    public LeaveType(){}
}
