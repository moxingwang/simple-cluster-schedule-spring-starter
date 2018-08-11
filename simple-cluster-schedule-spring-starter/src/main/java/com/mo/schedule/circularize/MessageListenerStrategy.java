package com.mo.schedule.circularize;

import com.mo.schedule.TaskMessageEventContainer;
import com.mo.schedule.entity.MessageEvent;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:20
 **/
public interface MessageListenerStrategy {
    String STRATEGY_REDIS_KEY = "com:mo:simple:cluster:schedule:broadcast";

    void handleMessage(String message);

    default void processMessage(TaskMessageEventContainer taskMessageEventContainer, MessageEvent messageEvent) {

    }

}
