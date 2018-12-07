package com.viktor.timeofftests.assertions;

import com.viktor.timeofftests.models.Company;
import com.viktor.timeofftests.models.User;

public class Assertions {
    public static UserAssert assertThat (User user){
        return new UserAssert(user);
    }
    public static CompanyAssert assertThat(Company company){
        return new CompanyAssert(company);
    }
}
