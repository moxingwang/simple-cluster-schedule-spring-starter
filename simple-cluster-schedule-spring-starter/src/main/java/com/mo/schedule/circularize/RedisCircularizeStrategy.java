package com.mo.schedule.circularize;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:17
 **/
public class RedisCircularizeStrategy implements CircularizeStrategy {

    private RedisTemplate redisTemplate;

    public RedisCircularizeStrategy(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
