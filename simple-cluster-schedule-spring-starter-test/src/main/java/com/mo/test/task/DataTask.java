package com.mo.test.task;

import com.mo.schedule.ScheduleClusterTask;
import com.mo.schedule.circularize.RedisCircularizeStrategy;
import com.mo.schedule.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 20:49
 **/
@Component
public class DataTask implements ScheduleClusterTask {
    private static final Logger logger = LoggerFactory.getLogger(RedisCircularizeStrategy.class);

    @Override
    public void start(Task task) {
        logger.info("DataTask开始调度");
    }
}
