package com.mo.lottery.controller;

import com.mo.schedule.SimpleScheduleClusterPublisher;
import com.mo.schedule.entity.RedisKey;
import com.mo.schedule.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Set;
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
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/test")
    public String test() throws ClassNotFoundException {
        Task task = new Task();
        task.setTaskId(UUID.randomUUID().toString());


/*        redisTemplate.opsForSet().add(RedisKey.REGISTRY_MACHINE_LIST+1, "1111111");
        redisTemplate.opsForSet().add(RedisKey.REGISTRY_MACHINE_LIST+1, "3333333333");
        Set<String> machines = redisTemplate.opsForSet().members(RedisKey.REGISTRY_MACHINE_LIST+1);


        redisTemplate.opsForSet().remove(RedisKey.REGISTRY_MACHINE_LIST+1, "1111111");
        Set<String> machines1 = redisTemplate.opsForSet().members(RedisKey.REGISTRY_MACHINE_LIST+1);

        redisTemplate.opsForSet().remove(RedisKey.REGISTRY_MACHINE_LIST+1, "3333333333");

        Set<String> machines2 = redisTemplate.opsForSet().members(RedisKey.REGISTRY_MACHINE_LIST+1);*/


//        task.setTaskClassName(CommonTask.);
        for (int i = 0; i < 200; i++) {
            simpleScheduleClusterPublisher.publishTask(Arrays.asList(task));
        }
        return "OK";
    }
}
