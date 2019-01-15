package com.viktor.timeofftests.models;

public enum LeaveDayPart {
    ALL(1),
    MORNING(2),
    AFTERNOON(3),
    ERROR(100);

    private final int value;
    LeaveDayPart (int value){
        this.value = value;
    }

    public static LeaveDayPart getValuesOf(int day_part_start) {
        switch (day_part_start){
            case(1): return LeaveDayPart.ALL;
            case(2): return LeaveDayPart.MORNING;
            case(3): return LeaveDayPart.AFTERNOON;
            default: return LeaveDayPart.ERROR;
        }
    }

    public int getValue() {
        return value;
    }
}
