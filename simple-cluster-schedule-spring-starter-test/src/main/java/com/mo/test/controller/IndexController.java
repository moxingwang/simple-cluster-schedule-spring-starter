package com.mo.test.controller;

import com.mo.schedule.SimpleScheduleClusterPublisher;
import com.mo.schedule.entity.Task;
import com.mo.test.task.CommonTask;
import com.mo.test.task.DataTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @description:
 * @author: MoXingwang 2018-08-13 19:18
 **/
@RestController
@RequestMapping("index")
public class IndexController {
    @Autowired
    private SimpleScheduleClusterPublisher simpleScheduleClusterPublisher;

    /**
     * 发布任务测试
     */
    @GetMapping("/push")
    public String push() {
        {
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                Task task = new Task();
                task.setTaskId(UUID.randomUUID().toString());
                task.setTaskName(CommonTask.class.getName());
                tasks.add(task);
            }
            simpleScheduleClusterPublisher.publishTask(tasks, CommonTask.class.getName());
        }
        {
            List<Task> tasks = new ArrayList<>();
            for (int i = 0; i < 200; i++) {
                Task task = new Task();
                task.setTaskId(UUID.randomUUID().toString());
                task.setTaskName(DataTask.class.getName());
                tasks.add(task);
            }
            simpleScheduleClusterPublisher.publishTask(tasks, DataTask.class.getName());
        }
        return "OK";
    }
}
