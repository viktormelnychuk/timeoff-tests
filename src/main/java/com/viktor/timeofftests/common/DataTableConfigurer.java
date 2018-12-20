package com.viktor.timeofftests.common;

import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pools.UserPool;
import cucumber.api.TypeRegistry;
import cucumber.api.TypeRegistryConfigurer;
import io.cucumber.datatable.DataTableType;
import io.cucumber.datatable.TableEntryTransformer;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

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
                User u = transformUser(map);
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
                    .inCompany(companyName)
                    .inDepartment(departmentName)
                    .build();
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
