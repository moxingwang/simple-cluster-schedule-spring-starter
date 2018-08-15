package com.mo.test.controller;

import com.mo.schedule.SimpleScheduleClusterPublisher;
import com.mo.schedule.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
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
     * 发布任务
     */
    @GetMapping("/push")
    public String push() {
        Task task = new Task();

        for (int i = 0; i < 200; i++) {
            task.setTaskId(UUID.randomUUID().toString());
            simpleScheduleClusterPublisher.publishTask(Arrays.asList(task));
        }
        return "OK";
    }
}
