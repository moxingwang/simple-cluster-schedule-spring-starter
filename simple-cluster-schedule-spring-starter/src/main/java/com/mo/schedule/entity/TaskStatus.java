package com.mo.schedule.entity;

/**
 * @description:
 * @author: MoXingwang 2018-08-12 22:57
 **/
public enum TaskStatus {

    UNEXECUTED(0),
    EXECUTING(1);

    private int value;

    TaskStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
