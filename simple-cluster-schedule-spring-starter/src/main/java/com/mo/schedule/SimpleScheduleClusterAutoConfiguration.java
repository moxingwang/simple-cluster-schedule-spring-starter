package com.mo.schedule;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:01
 **/

import com.mo.schedule.circularize.HeartbeatStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(ScheduleClusterProperties.class)
//@Import(RedisCircularizeStrategyConfiguration.class)
@ConditionalOnClass({TaskMessageEventContainer.class, HeartbeatStrategy.class})
//当RedisCircularizeStrategy在类路径中时并且当前容器中没有这个Bean的情况下,自动配置
public class SimpleScheduleClusterAutoConfiguration {

    @Autowired
    private ScheduleClusterProperties scheduleClusterProperties;

    @Bean
    @ConditionalOnMissingBean(TaskMessageEventContainer.class)//当容器中没有指定Bean的情况下
    public TaskMessageEventContainer defaultTaskMessageEventContainer(RedisTemplate redisTemplate) {
        TaskMessageEventContainer taskMessageEventContainer = new TaskMessageEventContainer();
        taskMessageEventContainer.setRedisTemplate(redisTemplate);
        return taskMessageEventContainer;
    }

    @Bean
    @ConditionalOnMissingBean(HeartbeatStrategy.class)//当容器中没有指定Bean的情况下
    public HeartbeatStrategy defaultHeartbeatStrategy(RedisTemplate redisTemplate) {
        HeartbeatStrategy heartbeatStrategy = new HeartbeatStrategy();
        return heartbeatStrategy;
    }

}
