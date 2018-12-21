package com.viktor.timeofftests.steps;

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
        typeRegistry.defineDataTableType(new DataTableType(User.class, new TableEntryTransformer<User>() {
            @Override
            public User transform(Map<String, String> map) throws Throwable {
                return transformUser(map);
            }
        }));
    }

    private User transformUser (Map<String, String> entry){
        try {
            String email = entry.get("email");
            String password = entry.get("password");
            String firstName = entry.get("first_name");
            String lastName = entry.get("last_name");
            boolean activated = transformToBoolean(entry.get("activated"));
            boolean admin = transformToBoolean(entry.get("admin"));
            boolean autoApprove = transformToBoolean(entry.get("auto_approve"));
            Date startDate = transformToDate(entry.get("started_on"));
            Date endDate = transformToDate(entry.get("ended_on"));
            String departmentName = entry.get("department");
            String companyName = entry.get("company");
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

    private boolean transformToBoolean(String s){
        if(s == null){
            return false;
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
