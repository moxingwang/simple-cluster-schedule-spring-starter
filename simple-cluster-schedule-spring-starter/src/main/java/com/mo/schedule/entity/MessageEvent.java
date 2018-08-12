package com.mo.schedule.entity;

import java.io.Serializable;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 18:57
 **/
public class MessageEvent implements Serializable {
    private int type;
    private String formIp;

    public String getFormIp() {
        return formIp;
    }

    public void setFormIp(String formIp) {
        this.formIp = formIp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
