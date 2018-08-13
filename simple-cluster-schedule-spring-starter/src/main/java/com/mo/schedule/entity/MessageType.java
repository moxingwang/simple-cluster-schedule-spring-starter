package com.mo.schedule.entity;

/**
 * @description:
 * @author: MoXingwang 2018-08-12 22:57
 **/
public enum MessageType {

    NEW_TASK_EVENT(0),
    EXECUTING(1);

    private int value;

    MessageType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
