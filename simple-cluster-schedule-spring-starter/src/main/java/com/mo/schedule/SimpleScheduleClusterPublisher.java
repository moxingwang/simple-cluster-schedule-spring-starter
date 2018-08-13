package com.mo.schedule;

import com.alibaba.fastjson.JSON;
import com.mo.schedule.entity.Task;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @description:
 * @author: MoXingwang 2018-08-13 13:20
 **/
public class SimpleScheduleClusterPublisher {
    private RedisTemplate redisTemplate;

    public SimpleScheduleClusterPublisher(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //发布任务
    public void publishTask(List<Task> tasks) {
        for (Task task : tasks) {
            redisTemplate.opsForSet().add(RedisKey.TASKS, JSON.toJSONString(task));
        }
    }

}
