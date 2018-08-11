package com.mo.schedule;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:01
 **/

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ScheduleClusterProperties.class)
//@Import(RedisCircularizeStrategyConfiguration.class)
@ConditionalOnClass(TaskMessageEventContainer.class)//当RedisCircularizeStrategy在类路径中时并且当前容器中没有这个Bean的情况下,自动配置
public class SimpleScheduleClusterAutoConfiguration {

    @Autowired
    private ScheduleClusterProperties scheduleClusterProperties;

    @Bean
    @ConditionalOnMissingBean(TaskMessageEventContainer.class)//当容器中没有指定Bean的情况下
    public TaskMessageEventContainer defaultTaskMessageEventContainer() {
        TaskMessageEventContainer taskMessageEventContainer = new TaskMessageEventContainer();
        return taskMessageEventContainer;
    }

}
