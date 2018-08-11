package com.mo.schedule.circularize;

import com.mo.schedule.util.IpUtil;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 23:38
 **/
public class HeartbeatStrategy {
    String HEARTBEAT_REGISTRY_REDIS_KEY = "com:mo:simple:cluster:schedule:heartbeat:registry";

    private RedisTemplate redisTemplate;

    /**
     * leader请求状态，状态正常0，发送验证1
     */
    private static final AtomicInteger leaderStatus = new AtomicInteger(0);
    /**
     * 本地leader
     */
    private static String localLeader = null;

    //启动后发送应用注册通知
    protected void registry(){
        redisTemplate.opsForSet().add(HEARTBEAT_REGISTRY_REDIS_KEY, IpUtil.getIp());
    }

    //定时向leader发送消息，收到leader的回复广播验证本地leader

}
