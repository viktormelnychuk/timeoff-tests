package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.UserService;

import static org.hamcrest.MatcherAssert.assertThat;
public class UserSteps extends BaseStep {
    private UserService userService = UserService.getInstance();

    public void validateUserExists(String email){
        User user = userService
    }
}
