package com.viktor.timeofftests.services;

import com.viktor.timeofftests.models.User;

public class UserService {
    public User createNewUser(String email, String password){
        User user = new User.Builder()
                .withEmail(email)
                .withPassword(password)
                .withName("User1")
                .withLastName("User2")
                .build();
        return user;
    }
}
