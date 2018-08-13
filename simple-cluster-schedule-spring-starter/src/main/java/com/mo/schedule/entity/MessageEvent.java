package com.mo.schedule.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 18:57
 **/
public class MessageEvent implements Serializable {
    private int type;
    private String formId;
    private String toId;
    private List<Task> tasks;


    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
