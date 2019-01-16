package com.viktor.timeofftests.common;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class DateUtil {
    public static LocalDate selectStartDay() {
        //selecting next working day as start day
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        if(dayOfWeek.getValue() == 6 ){
            return now.plusDays(2);
        } else if(dayOfWeek.getValue() == 7){
            return now.plusDays(1);
        } else {
            return now;
        }
    }

    public static LocalDate selectEndDay(LocalDate startDate, int amountOfDays) {
        LocalDate endDate = startDate;
        boolean done = false;
        int daysAdded = 0;
        LocalDate currentDate = startDate;
        while(!done){
            if(daysAdded == amountOfDays-1) done = true;
            currentDate = currentDate.plusDays(1);
            if(currentDate.getDayOfWeek().getValue() == 6 || currentDate.getDayOfWeek().getValue() == 7){
                continue;
            }
            endDate = currentDate;
            daysAdded++;
        }
        return endDate;
    }
}
