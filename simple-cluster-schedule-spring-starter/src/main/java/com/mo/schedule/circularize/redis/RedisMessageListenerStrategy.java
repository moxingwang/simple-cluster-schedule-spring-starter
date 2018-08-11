package com.mo.schedule.circularize.redis;

import com.mo.schedule.circularize.MessageListenerStrategy;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:33
 **/
public class RedisMessageListenerStrategy implements MessageListenerStrategy {

    @Override
    public void handleMessage(String message) {
        System.out.println(message);
    }
}
