package com.viktor.timeofftests.forms;

import lombok.Data;

@Data
public class CompanySettingsForm {
    private String companyName;
    private String country;
    private String dateFormat;
    private String timezone;
}
