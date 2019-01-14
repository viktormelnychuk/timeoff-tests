package com.viktor.timeofftests.models;

public enum LeaveDayPart {
    ALL(1),
    MORNING(2),
    AFTERNOON(3);
    private final int value;
    LeaveDayPart (int value){
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
