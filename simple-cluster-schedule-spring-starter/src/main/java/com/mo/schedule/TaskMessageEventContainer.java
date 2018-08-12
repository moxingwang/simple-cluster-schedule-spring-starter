package com.mo.schedule;

import com.alibaba.fastjson.JSON;
import com.mo.schedule.circularize.CircularizeStrategy;
import com.mo.schedule.entity.Task;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:08
 **/
public class TaskMessageEventContainer {

    private RedisTemplate redisTemplate;
    private CircularizeStrategy circularizeStrategy;

    //发布任务
    public void publishTask(List<Task> tasks) {
        for (Task task : tasks) {
            redisTemplate.opsForSet().add(RedisKey.TASKS_KEY, JSON.toJSONString(task));
        }
    }


    //收到新任务

    //发送撤回任务通知

    //收到撤回任务通知

    //发送撤回本地任务

    //收到撤回本地任务{master收到才会处理}

    //发布新的任务列表

    //收到新的任务列表{master收到才会处理}

    //master定时巡检和重新编排任务


    public void setCircularizeStrategy(CircularizeStrategy circularizeStrategy) {
        this.circularizeStrategy = circularizeStrategy;
    }

    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
