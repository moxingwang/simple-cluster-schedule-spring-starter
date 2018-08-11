package com.mo.schedule.circularize;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:20
 **/
public interface MessageListenerStrategy {
    String STRATEGY_REDIS_KEY = "com:mo:simple:cluster:schedule:broadcast";

    void handleMessage(String message);

}
