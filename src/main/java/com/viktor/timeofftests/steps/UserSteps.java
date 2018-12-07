package com.viktor.timeofftests.steps;

import com.viktor.timeofftests.models.User;
import com.viktor.timeofftests.services.UserService;
import io.qameta.allure.Step;

import static org.assertj.core.api.Assertions.*;

public class UserSteps extends BaseStep {
    private UserService userService = UserService.getInstance();
    private static UserSteps instance;

    public static UserSteps getInstance(){
        if(instance == null){
            instance = new UserSteps();
            return instance;
        } else {
            return instance;
        }
    }

    @Step("Validating user with email {email} exists")
    public void validateUserExists(String email){
        User user = userService.getUserWithEmail(email);
        assertThat(user).isNotNull();
    }

    public void validateUserIsAdmin(String email){
        User user = userService.getUserWithEmail(email);
        assertThat(user.isAdmin()).isEqualTo(true);
    }


}
