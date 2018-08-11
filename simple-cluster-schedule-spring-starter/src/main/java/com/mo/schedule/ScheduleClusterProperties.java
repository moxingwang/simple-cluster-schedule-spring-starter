package com.mo.schedule;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @description: 配置文件读取
 * @author: MoXingwang 2018-08-11 14:47
 **/

@ConfigurationProperties(prefix = "simple.schedule.cluster")
public class ScheduleClusterProperties implements BeanClassLoaderAware, InitializingBean {

    private Integer taskCount = 10;

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public void setBeanClassLoader(ClassLoader classLoader) {

    }

    public void afterPropertiesSet() throws Exception {

    }
}
