package com.mo.schedule.circularize;

import com.alibaba.fastjson.JSON;
import com.mo.schedule.RedisKey;
import com.mo.schedule.entity.MessageEvent;
import com.mo.schedule.entity.Task;
import com.mo.schedule.util.IpUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:17
 **/
public class RedisCircularizeStrategy {

    private RedisTemplate redisTemplate;

    public static final int MESSAGE_TYPE_TASK_PUSH = 0;

    /**
     * 向leader汇报任务状态，正常0，已发送等待回复1
     */
    private static final AtomicInteger leaderStatus = new AtomicInteger(0);
    /**
     * 本地leader缓存
     */
    private String localLeader = null;

    //定时向leader发送消息，收到leader的回复广播验证本地leader


    public String getLocalLeader() {
        return localLeader;
    }

    //定时汇报任务
    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        leaderStatus.set(1);
        MessageEvent messageEvent = new MessageEvent();

        //获取本地未执行任务

        messageEvent.setType(MESSAGE_TYPE_TASK_PUSH);
        sendBroadcast(messageEvent);
    }


    public void sendBroadcast(MessageEvent messageEvent) {
        redisTemplate.convertAndSend(RedisKey.STRATEGY_REDIS_KEY_BROADCAST, messageEvent);
    }


    //启动后发送应用注册通知
    public RedisCircularizeStrategy(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisTemplate.opsForSet().add(RedisKey.HEARTBEAT_REGISTRY_REDIS_KEY, IpUtil.getIp());
    }

    //发布任务
    public void publishTask(List<Task> tasks) {
        for (Task task : tasks) {
            redisTemplate.opsForSet().add(RedisKey.TASKS_KEY, JSON.toJSONString(task));
        }
    }

}
