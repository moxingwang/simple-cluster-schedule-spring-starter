package com.mo.schedule;

import com.mo.schedule.circularize.RedisMessageListenerStrategy;
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
    MessageListenerAdapter messageListener(RedisTemplate redisTemplate) {
        return new MessageListenerAdapter(new RedisMessageListenerStrategy(redisTemplate));
    }

    @Bean
    RedisMessageListenerContainer redisContainer(RedisTemplate redisTemplate,RedisConnectionFactory factory) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(factory);
        container.addMessageListener(messageListener(redisTemplate), new PatternTopic(RedisKey.STRATEGY_REDIS_KEY_BROADCAST));
        return container;
    }


}
