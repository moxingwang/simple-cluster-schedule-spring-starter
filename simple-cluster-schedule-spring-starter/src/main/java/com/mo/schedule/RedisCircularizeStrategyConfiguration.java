package com.mo.schedule;

import com.mo.schedule.circularize.RedisCircularizeStrategy;
import com.mo.schedule.circularize.RedisMessageListenerStrategy;
import com.mo.schedule.entity.RedisKey;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:17
 **/
@Configuration
public class RedisCircularizeStrategyConfiguration {

    @Bean
    MessageListenerAdapter messageListener(RedisTemplate redisTemplate, RedisCircularizeStrategy redisCircularizeStrategy) {
        return new MessageListenerAdapter(new RedisMessageListenerStrategy(redisTemplate, redisCircularizeStrategy));
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisTemplate redisTemplate, RedisConnectionFactory factory, RedisCircularizeStrategy redisCircularizeStrategy) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListener(redisTemplate, redisCircularizeStrategy), new PatternTopic(RedisKey.STRATEGY_BROADCAST));
        return container;
    }


}
