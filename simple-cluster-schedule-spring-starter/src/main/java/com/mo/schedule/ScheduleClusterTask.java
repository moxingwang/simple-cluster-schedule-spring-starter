package com.mo.schedule;

import com.mo.schedule.entity.Task;

/**
 * @description: 任务调度
 * @author: MoXingwang 2018-08-11 12:37
 **/
public interface ScheduleClusterTask {
    /**
     * 任务调度入口
     */
    void start(Task task);
}
