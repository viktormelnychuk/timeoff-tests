package com.viktor.timeofftests.models;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.Map;

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
    public LeaveType (Map<String, String> data){
        this.name = data.get("name");
        if(StringUtils.isNotEmpty(data.get("color"))){
            this.colorHex = "leave_type_" + data.get("color").replaceAll(" ","_").toLowerCase();
        } else {
            this.colorHex = "leave_type_color_1";
        }

        if(StringUtils.isNotEmpty(data.get("use_allowance"))){
            if(data.get("use_allowance").equals("true")){
                this.useAllowance = true;
            } else {
                this.useAllowance = false;
            }
        }

        if(StringUtils.isNotEmpty(data.get("limit"))){
            this.limit = Integer.parseInt(data.get("limit"));
        } else {
            this.limit = 10;
        }
    }
}
