package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.pools.UserPool;
import io.cucumber.datatable.DataTable;

public class UserSteps {
    public static User createUser(DataTable table){

        User user = (User) table.asList(User.class).get(0);
        if(user.getLastName() == null){
            user.setLastName(UserPool.getLastName());
        }
        if(user.getName() == null){
            user.setName(UserPool.getName());
        }
        if(user.getEmail() == null){
            user.setEmail(UserPool.getEmail());
        }
        //
        return user;
    }
}
