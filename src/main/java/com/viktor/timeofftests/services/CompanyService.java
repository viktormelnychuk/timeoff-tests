package com.viktor.timeofftests.services;

import com.viktor.timeofftests.models.Company;

public class CompanyService {
    private static CompanyService companyService;

    public static CompanyService getInstance(){
        if(companyService == null){
            return new CompanyService();
        } else {
            return companyService;
        }
    }
    private CompanyService(){}
}
