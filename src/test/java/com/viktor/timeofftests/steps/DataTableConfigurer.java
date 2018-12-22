package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.common.Constants;
import com.viktor.timeofftests.forms.SignupForm;
import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pools.UserPool;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

import java.util.*;

public class DataTableConfigurer implements TypeRegistryConfigurer {
    @Override
    public Locale locale() {
        return Locale.ENGLISH;
    }

    @Override
    public void configureTypeRegistry(TypeRegistry typeRegistry) {
        typeRegistry.defineDataTableType(new DataTableType(User.class, this::transformUser));
        typeRegistry.defineDataTableType(new DataTableType(SignupForm.class,this::transformToSignupForm));
    }

    private SignupForm transformToSignupForm(Map<String, String> entry){
        SignupForm form = new SignupForm();
        String companyName = entry.get("currentCompany");
        String firstName = entry.get("first_name");
        String lastName = entry.get("last_name");
        String email = entry.get("email");
        String password = entry.get("password");
        String passwordConfirmation = entry.get("password_confirmation");
        String country = entry.get("country");
        String timezone = entry.get("timezone");
        if(companyName == null){
            companyName = Constants.DEFAULT_COMPANY_NAME;
        }
        if (firstName == null){
            firstName = Constants.DEFAULT_USER_NAME;
        }
        if(lastName == null){
            lastName = Constants.DEFAULT_USER_LAST_NAME;
        }
        if(email == null){
            email = Constants.DEFAULT_USER_EMAIL;
        }
        if(password == null){
            password = Constants.DEFAULT_USER_PASSWORD;
        }
        if(passwordConfirmation == null){
            passwordConfirmation = Constants.DEFAULT_USER_PASSWORD;
        }

        if(country == null){
            country = Constants.DEFAULT_COMPANY_COUNTRY;
        }
        if(timezone == null){
            timezone = Constants.DEFAULT_COMPANY_TIMEZONE;
        }
        form.setCompanyName(companyName);
        form.setFirstName(firstName);
        form.setLastName(lastName);
        form.setEmail(email);
        form.setPassword(password);
        form.setPasswordConfirmation(passwordConfirmation);
        form.setCountry(country);
        form.setTimezone(timezone);
        return form;
    }

    private User transformUser (Map<String, String> entry){
        try {
            String email = entry.get("email");
            String password = entry.get("password");
            String firstName = entry.get("first_name");
            String lastName = entry.get("last_name");
            boolean activated = transformToBoolean(entry.get("activated"), true);
            boolean admin = transformToBoolean(entry.get("admin"), false);
            boolean autoApprove = transformToBoolean(entry.get("auto_approve"), true);
            Date startDate = transformToDate(entry.get("started_on"));
            Date endDate = transformToDate(entry.get("ended_on"));
            String departmentName = entry.get("department");
            String companyName = entry.get("currentCompany");
            if (email == null) {
                email = UserPool.getEmail();
            }
            if (firstName == null) {
                firstName = UserPool.getName();
            }
            if (lastName == null) {
                lastName = UserPool.getLastName();
            }
            if(!activated){
                Calendar calendar = Calendar.getInstance();
                calendar.roll(Calendar.DAY_OF_YEAR, -10);
                // Roll 10 days back from today
                startDate = calendar.getTime();
                // Roll 9 days from startedOn
                calendar.roll(Calendar.DAY_OF_YEAR, 9);
                endDate = calendar.getTime();
            }
            if(startDate == null){
                startDate = new Date();
            }
            User user = new User.Builder()
                    .withEmail(email)
                    .withName(firstName)
                    .withLastName(lastName)
                    .withPassword(password)
                    .activated(activated)
                    .admin(admin)
                    .autoApproved(autoApprove)
                    .startedOn(startDate)
                    .endedOn(endDate)
                    .build();
            user.setDepartmentName(departmentName);
            user.setCompanyName(companyName);
            return user;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private boolean transformToBoolean(String s, boolean defaultNull){
        if(s == null){
            return defaultNull;
        }
        return Objects.equals(s, "true");
    }

    private Date transformToDate(String s){
        if(s == null){
            return null;
        } else {
            // add logic to parse string
            return new Date();
        }

    }
}
