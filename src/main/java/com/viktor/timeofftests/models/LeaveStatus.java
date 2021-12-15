package com.viktor.timeofftests.models;

public enum LeaveStatus {
    NEW (1),
    APPROVED(2),
    REJECTED(3),
    PENDED_REVOKE(4),
    CANCELLED(5),
    ERROR(100);

    private final int value;
    LeaveStatus(int value){
        this.value = value;
    }
    public int getValue(){
        return value;
    }
    public static LeaveStatus getByValue(int v){
        switch (v){
            case(1): return LeaveStatus.NEW;
            case(2): return LeaveStatus.APPROVED;
            case(3): return LeaveStatus.REJECTED;
            case(4): return LeaveStatus.PENDED_REVOKE;
            case(5): return LeaveStatus.CANCELLED;
            default: return LeaveStatus.ERROR;
        }
    }
}
