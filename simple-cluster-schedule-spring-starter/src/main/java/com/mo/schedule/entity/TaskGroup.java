package com.mo.schedule.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 18:25
 **/
public class TaskGroup implements Serializable {
    private String name;
    private List<Task> tasks;

    public TaskGroup(String name, List<Task> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
