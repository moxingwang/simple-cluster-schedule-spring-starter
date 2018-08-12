package com.mo.schedule.circularize;

import com.mo.schedule.entity.MessageEvent;

/**
 * @description: 集群之间的通知
 * @author: MoXingwang 2018-08-11 15:18
 **/
@FunctionalInterface
public interface CircularizeStrategy {
    /**
     * 发送通知
     *
     * @param messageEvent
     */
    void sendMessage(MessageEvent messageEvent);


}
