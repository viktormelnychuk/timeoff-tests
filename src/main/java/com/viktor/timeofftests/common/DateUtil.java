package com.viktor.timeofftests.common;

import lombok.extern.log4j.Log4j2;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Log4j2
public class DateUtil {
    public static LocalDate selectStartDay() {
        log.info("Selecting start day for leave to be next working day");
        //selecting next working day as start day
        LocalDate startDate;
        LocalDate now = LocalDate.now();
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        if(dayOfWeek.getValue() == 6 ){
            startDate = now.plusDays(2);
        } else if(dayOfWeek.getValue() == 7){
            startDate = now.plusDays(1);
        } else {
            startDate = now;
        }
        log.info("Start day is [{}]", startDate);
        return startDate;
    }

    public static LocalDate selectEndDay(LocalDate startDate, int amountOfDays) {
        log.info("Selecting end date that is [{}] working days after [{}]", amountOfDays, startDate);
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
        log.info("End date is [{}]",endDate);
        return endDate;
    }
}
