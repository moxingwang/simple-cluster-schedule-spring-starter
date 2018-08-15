package com.mo.schedule.config;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:01
 **/

import com.mo.schedule.ScheduleClusterProperties;
import com.mo.schedule.SimpleScheduleClusterPublisher;
import com.mo.schedule.TaskContainer;
import com.mo.schedule.circularize.RedisCircularizeStrategy;
import com.mo.schedule.config.RedisCircularizeStrategyConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@EnableConfigurationProperties(ScheduleClusterProperties.class)
@Import(RedisCircularizeStrategyConfiguration.class)
//当RedisCircularizeStrategy在类路径中时并且当前容器中没有这个Bean的情况下,自动配置
@ConditionalOnClass({TaskContainer.class, RedisCircularizeStrategy.class, SimpleScheduleClusterPublisher.class})
public class SimpleScheduleClusterAutoConfiguration {

    @Autowired
    private ScheduleClusterProperties scheduleClusterProperties;

    @Bean
    @ConditionalOnMissingBean(TaskContainer.class)//当容器中没有指定Bean的情况下
    public TaskContainer defaultTaskContainer(RedisTemplate redisTemplate, ApplicationContext applicationContext) {
        TaskContainer taskContainer = new TaskContainer(redisTemplate, applicationContext);
        return taskContainer;
    }

    @Bean
    @ConditionalOnMissingBean(RedisCircularizeStrategy.class)
    //使用者可以使用@Qualifier("redisTemplate") RedisTemplate这种方式区分具体某个RedisTemplate bean
    public RedisCircularizeStrategy defaultRedisCircularizeStrategy(RedisTemplate redisTemplate, TaskContainer taskContainer) {
        RedisCircularizeStrategy redisCircularizeStrategy = new RedisCircularizeStrategy(redisTemplate, taskContainer);
        return redisCircularizeStrategy;
    }


    @Bean
    @ConditionalOnMissingBean(SimpleScheduleClusterPublisher.class)
    public SimpleScheduleClusterPublisher defaultSimpleScheduleClusterPublisher(RedisTemplate redisTemplate) {
        SimpleScheduleClusterPublisher simpleScheduleClusterPublisher = new SimpleScheduleClusterPublisher(redisTemplate,scheduleClusterProperties.getPackageName());
        return simpleScheduleClusterPublisher;
    }
}
