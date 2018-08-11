package com.mo.schedule.circularize.redis;

import com.mo.schedule.circularize.MessageListenerStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:17
 **/
@Configuration
public class RedisCircularizeStrategyConfiguration {

    //初始化监听器
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic(MessageListenerStrategy.STRATEGY_REDIS_KEY));
        return container;
    }

    @Bean
    RedisMessageListenerStrategy get() {
        return new RedisMessageListenerStrategy();
    }

    @Bean
    MessageListenerAdapter listenerAdapter(RedisMessageListenerStrategy redisReceiver) {
        return new MessageListenerAdapter(redisReceiver);
    }


}
