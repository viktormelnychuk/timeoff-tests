package com.viktor.timeofftests.models;

public enum LeaveStatus {
    NEW (1),
    APPROVED(2),
    REJECTED(3),
    PENDED_REVOKE(4),
    CANCELLED(5);

    private final int value;
    LeaveStatus (int value){
        this.value =value;
    }
    public int getValue(){
        return value;
    }
}
