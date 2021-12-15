package com.viktor.timeofftests.forms;

import lombok.Data;

@Data
public class WeeklyScheduleForm {
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
}
