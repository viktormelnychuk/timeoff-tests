package com.viktor.timeofftests.pools;

import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;

public class DepartmentPool {
    private static Faker faker = new Faker();
    private static List<String> availableNames = new ArrayList<>();
    public static String getRandomDepartmentName(){
        return faker.team().name();
    }

    public static String getDepartmentName(){
        if(availableNames.size() == 0){
            for(int i = 0; i < 4; i++){
                availableNames.add(getRandomDepartmentName());
            }
        }
        return availableNames.get((int)(Math.random()*availableNames.size()));
    }
}
