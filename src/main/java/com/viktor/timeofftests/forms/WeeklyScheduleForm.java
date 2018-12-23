package com.viktor.timeofftests.forms;

import lombok.Data;

@Data
public class WeeklyScheduleForm {
    private int monday = 1;
    private int tuesday = 1;
    private int wednesday = 1;
    private int thursday = 1;
    private int friday = 1;
    private int saturday = 2;
    private int sunday = 2;
}
