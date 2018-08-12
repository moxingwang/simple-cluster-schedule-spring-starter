package com.mo.schedule.circularize;

import com.alibaba.fastjson.JSON;
import com.mo.schedule.RedisKey;
import com.mo.schedule.entity.MessageEvent;
import com.mo.schedule.entity.Task;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:17
 **/
public class RedisCircularizeStrategy {
    //每个应用启动后的唯一标识
    public static final String MACHINE_ID = UUID.randomUUID().toString();

    private RedisTemplate redisTemplate;

    public static final int MESSAGE_TYPE_TASK_PUSH = 0;

    /**
     * 本地leader缓存
     */
    private String leaderID = null;

    //定时向leader发送消息，收到leader的回复广播验证本地leader


    //定时任务处理
    @Scheduled(fixedRate = 10000)
    public void reportCurrentTime() {
        if (!isLeader()) {
            //定时更新本地自己的任务状态到redis

        } else {
            //编排任务: 获取所有任务，做统一编排
            Set<String> machines = redisTemplate.opsForSet().members(RedisKey.REGISTRY_MACHINE_LIST);


            //再分派任务的时候检测对应follower的状态{follower收到任务并且应答，说明follower还live}

        }

    }

    //leader心跳
    @Scheduled(fixedRate = 3000)
    public void leaderHeartbeat() {
        if (isLeader()) {
            //维持心跳
            redisTemplate.opsForValue().set(RedisKey.LEADER, MACHINE_ID, 5, TimeUnit.SECONDS);
        } else {
            //检测leader的存在性
            Object leader = redisTemplate.opsForValue().get(RedisKey.LEADER);
            if (leader == null) {
                //leader已不存在,自己竞选leader
                boolean success = redisTemplate.opsForValue().setIfAbsent(RedisKey.LEADER, MACHINE_ID);
                if (success) {
                    redisTemplate.opsForValue().set(RedisKey.LEADER, MACHINE_ID, 5, TimeUnit.SECONDS);
                    leaderID = MACHINE_ID;
                }
            }
        }
    }


    public void sendBroadcast(MessageEvent messageEvent) {
        redisTemplate.convertAndSend(RedisKey.STRATEGY_BROADCAST, messageEvent);
    }


    //启动后发送应用注册通知
    public RedisCircularizeStrategy(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        redisTemplate.opsForSet().add(RedisKey.REGISTRY_MACHINE_LIST, RedisCircularizeStrategy.MESSAGE_TYPE_TASK_PUSH);


        //key永久存储
        redisTemplate.expire(RedisKey.REGISTRY_MACHINE_LIST, Integer.MAX_VALUE, TimeUnit.DAYS);
    }

    //发布任务
    public void publishTask(List<Task> tasks) {
        for (Task task : tasks) {
            redisTemplate.opsForSet().add(RedisKey.TASKS_OWNER + MACHINE_ID, JSON.toJSONString(task));
        }
    }

    public boolean isLeader() {
        return MACHINE_ID.equals(leaderID);
    }


}
