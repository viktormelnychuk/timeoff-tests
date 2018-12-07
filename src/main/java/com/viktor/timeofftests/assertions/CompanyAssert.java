package com.viktor.timeofftests.assertions;

import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.services.CompanyService;
import org.assertj.core.api.AbstractObjectAssert;

public class CompanyAssert extends AbstractObjectAssert<CompanyAssert, Company> {
    private CompanyService companyService = CompanyService.getInstance();
    public CompanyAssert (Company actual){
        super(actual, CompanyAssert.class);
    }

    public CompanyAssert exists(){
        isNotNull();
        Company companyInDb = companyService.getCompanyWithName(actual.getName());
        if(companyInDb == null){
            failWithMessage("Company with name <%s> does not exist in db", actual.getName());
        }
        return this;
    }
}
