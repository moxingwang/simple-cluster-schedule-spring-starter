package com.mo.schedule.circularize.redis;

import com.mo.schedule.circularize.CircularizeStrategy;
import com.mo.schedule.entity.MessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:17
 **/
@Service
public class RedisCircularizeStrategy implements CircularizeStrategy {
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void sendMessage(MessageEvent messageEvent) {

    }

}
