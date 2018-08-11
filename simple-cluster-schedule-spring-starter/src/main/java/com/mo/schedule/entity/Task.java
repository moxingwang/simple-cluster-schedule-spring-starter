package com.mo.schedule.entity;

import java.io.Serializable;

/**
 * @description: 任务
 * @author: MoXingwang 2018-08-11 12:30
 **/
public class Task implements Serializable {
    private String taskName;
    private String taskGroupName;
    private String taskClassName;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskGroupName() {
        return taskGroupName;
    }

    public void setTaskGroupName(String taskGroupName) {
        this.taskGroupName = taskGroupName;
    }

    public String getTaskClassName() {
        return taskClassName;
    }

    public void setTaskClassName(String taskClassName) {
        this.taskClassName = taskClassName;
    }
}
