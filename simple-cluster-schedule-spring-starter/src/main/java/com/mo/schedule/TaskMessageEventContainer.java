package com.mo.schedule;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:08
 **/
public class TaskMessageEventContainer {

    private RedisTemplate redisTemplate;

    int MESSAGE_TYPE_SEND = 0;



    //发送新任务

    //收到新任务

    //发送撤回任务通知

    //收到撤回任务通知

    //发送撤回本地任务

    //收到撤回本地任务{master收到才会处理}

    //发布新的任务列表

    //收到新的任务列表{master收到才会处理}

    //master定时巡检和重新编排任务


    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
