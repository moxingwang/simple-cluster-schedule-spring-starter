package com.mo.lottery.task;

import com.mo.schedule.ScheduleClusterTask;
import com.mo.schedule.annotation.EnableScheduleClusterTask;
import com.mo.schedule.entity.Task;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 20:49
 **/
@EnableScheduleClusterTask
@Component
public class CommonTask implements ScheduleClusterTask {
    @Override
    public void start(Task task) {

    }
}
