package com.viktor.timeofftests.pools;

import com.github.javafaker.Faker;

public class UserPool {
    private static Faker faker = new Faker();
    public static String getEmail(){
        return faker.internet().emailAddress();
    }

    public static String getName(){
        return faker.name().firstName();
    }

    public static String getLastName(){
        return faker.name().lastName();
    }
}
