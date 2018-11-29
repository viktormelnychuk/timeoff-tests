package com.viktor.timeofftests.models;

import com.viktor.timeofftests.common.Constants;
import lombok.Data;
import lombok.extern.log4j.Log4j2;

@Data
public class Company {
    private int id;
    private String name;
    private String country;
    private int startOfNewYear;
    private boolean shareAllAbsences;
    private boolean ldapAuthEnabled;
    private String ldapAuthConfig;
    private String dateFormat;
    private String companyWideMessage;
    private int mode;
    private String timezone;

    private Company(){}
    @Log4j2
    public static class Builder{
        private int id;
        private String name;
        private String country = Constants.DEFAULT_COMPANY_COUNTRY;
        private int startOfNewYear = 1;
        private boolean shareAllAbsences = false;
        private boolean ldapAuthEnabled = false;
        private String ldapAuthConfig;
        private String dateFormat = "YYYY-MM-DD";
        private String companyWideMessage;
        private int mode = 1;
        private String timezone = Constants.DEFAULT_COMPANY_TIMEZONE;

        public Builder withId(int id){
            this.id = id;
            return this;
        }
        public Builder withName(String name){
            this.name = name;
            return this;
        }

        public Builder withCountry(String country){
            this.country = country;
            return this;
        }

        public Builder withStartOfNewYear(int startOfNewYear){
            this.startOfNewYear = startOfNewYear;
            return this;
        }

        public Builder shareAllAbsences(boolean shareAllAbsences){
            this.shareAllAbsences = shareAllAbsences;
            return this;
        }
        public Builder ldapAuthEnabled(boolean ldapAuthEnabled){
            this.ldapAuthEnabled = ldapAuthEnabled;
            return this;
        }

        public Builder ldapAuthConfig (String config){
            this.ldapAuthConfig = config;
            return this;
        }
        public Builder withDateFormat(String dateFormat){
            this.dateFormat = dateFormat;
            return this;
        }

        public Builder withCompanyWideMessage (String message){
            this.companyWideMessage = message;
            return this;
        }

        public Builder withMode(int mode){
            this.mode = mode;
            return this;
        }

        public Builder withTimeZone(String timezone){
            this.timezone = timezone;
            return this;
        }

        public Company build(){
            Company company = new Company();
            company.name = this.name;
            company.country = this.country;
            company.startOfNewYear = this.startOfNewYear;
            company.shareAllAbsences = this.shareAllAbsences;
            company.ldapAuthEnabled = this.ldapAuthEnabled;
            company.ldapAuthConfig = this.ldapAuthConfig;
            company.dateFormat = this.dateFormat;
            company.companyWideMessage = this.companyWideMessage;
            company.mode = this.mode;
            company.timezone = this.timezone;
            company.id = this.id;
            return company;
        }
    }
}
