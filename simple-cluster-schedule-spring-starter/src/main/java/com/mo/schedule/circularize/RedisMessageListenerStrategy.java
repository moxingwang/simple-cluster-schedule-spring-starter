package com.mo.schedule.circularize;

import com.mo.schedule.entity.MessageEvent;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:33
 **/
public class RedisMessageListenerStrategy implements MessageListener {
    private RedisTemplate redisTemplate;
    private RedisCircularizeStrategy redisCircularizeStrategy;

    public RedisMessageListenerStrategy(RedisTemplate redisTemplate, RedisCircularizeStrategy redisCircularizeStrategy) {
        this.redisTemplate = redisTemplate;
        this.redisCircularizeStrategy = redisCircularizeStrategy;
    }

    @Override
    public void onMessage(Message message, byte[] bytes) {
        MessageEvent messageEvent = (MessageEvent) redisTemplate.getValueSerializer().deserialize(message.getBody());

        //只处理自己的广播
        if (RedisCircularizeStrategy.MACHINE_ID.equals(messageEvent.getToId())) {
            redisCircularizeStrategy.onMessage(messageEvent);
        }
    }
}