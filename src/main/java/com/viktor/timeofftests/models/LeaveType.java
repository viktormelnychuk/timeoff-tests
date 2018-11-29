package com.viktor.timeofftests.models;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class LeaveType {
    private String name;
    private String colorHex;
    private boolean useAllowance;
    private int limit;
    private int sortOrder;
    private int companyId;
    private Timestamp createdAt = new Timestamp(new java.util.Date().getTime());
    private Timestamp updatedAt = new Timestamp(new java.util.Date().getTime());

    public LeaveType(String name, String colorHex, boolean useAllowance, int limit, int sortOrder, int companyId){
        this.name = name;
        this.colorHex = colorHex;
        this.useAllowance = useAllowance;
        this.limit = limit;
        this.sortOrder = sortOrder;
        this.companyId = companyId;
    }
}
