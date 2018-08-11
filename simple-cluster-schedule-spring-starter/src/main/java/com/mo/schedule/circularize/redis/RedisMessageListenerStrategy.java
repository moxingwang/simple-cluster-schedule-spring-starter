package com.mo.schedule.circularize.redis;

import com.mo.schedule.TaskMessageEventContainer;
import com.mo.schedule.circularize.MessageListenerStrategy;
import com.mo.schedule.entity.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:33
 **/
public class RedisMessageListenerStrategy implements MessageListenerStrategy {

    @Autowired
    private TaskMessageEventContainer taskMessageEventContainer;

    @Override
    public void handleMessage(String message) {
        System.out.println(message);

        processMessage(taskMessageEventContainer, new MessageEvent());

    }
}
