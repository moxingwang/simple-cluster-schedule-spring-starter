package com.mo.schedule.circularize;

import com.alibaba.fastjson.JSON;
import com.mo.schedule.TaskContainer;
import com.mo.schedule.entity.MessageEvent;
import com.mo.schedule.entity.MessageType;
import com.mo.schedule.entity.RedisKey;
import com.mo.schedule.entity.Task;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 15:17
 **/
public class RedisCircularizeStrategy {
    //每个应用启动后的唯一标识
    public static final String MACHINE_ID = UUID.randomUUID().toString();

    private RedisTemplate redisTemplate;
    private TaskContainer taskContainer;

    /**
     * 本地leader缓存
     */
    private String leaderID = null;

    //定时向leader发送消息，收到leader的回复广播验证本地leader


    @Scheduled(fixedRate = 3000)
    public void leaderHeartbeat() {
        //心跳维持
        if (isLeader()) {
            redisTemplate.opsForValue().set(RedisKey.LEADER, MACHINE_ID, 5, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(RedisKey.FOLLOWER + MACHINE_ID, "1", 5, TimeUnit.SECONDS);
        }

        if (isLeader()) {
            //编排任务; 检查follower心跳
            Set<String> machines = redisTemplate.opsForSet().members(RedisKey.REGISTRY_MACHINE_LIST);

            //检查machines的心跳
            Set<String> realMachines = new HashSet<>();
            for (String machine : machines) {
                if (!MACHINE_ID.equals(machine)) {
                    Object machineObj = redisTemplate.opsForValue().get(RedisKey.FOLLOWER + machine);
                    if (null == machineObj) {
                        //清除这台机器并且收回所有任务

                        //todo 添加事务
                        Set<Task> tasks = redisTemplate.opsForSet().members(RedisKey.TASKS_OWNER + machine);
                        if (!CollectionUtils.isEmpty(tasks)) {
                            for (Task task : tasks) {
                                redisTemplate.opsForSet().add(RedisKey.TASKS, task);
                            }
                        }

                        redisTemplate.opsForSet().remove(RedisKey.REGISTRY_MACHINE_LIST, machine);
                    }else {
                        realMachines.add(machine);
                    }
                }
            }

            Map<String, Long> arrange = new HashMap<>();

            for (String machine : realMachines) {
                arrange.put(machine, redisTemplate.opsForSet().size(RedisKey.TASKS_OWNER + machine));
            }

            arrangeTasks(arrange);
        } else {
            //检测leader的存在性
            Object leader = redisTemplate.opsForValue().get(RedisKey.LEADER);
            if (leader == null) {
                //leader已不存在,自己竞选leader
                boolean success = redisTemplate.opsForValue().setIfAbsent(RedisKey.LEADER, MACHINE_ID);
                if (success) {
                    redisTemplate.opsForValue().set(RedisKey.LEADER, MACHINE_ID, 5, TimeUnit.SECONDS);
                    leaderID = MACHINE_ID;
                }
            }
        }
    }

    private void arrangeTasks(Map<String, Long> arrange) {
        Long totalUnExeTaskSize = redisTemplate.opsForSet().size(RedisKey.TASKS);
        if (totalUnExeTaskSize <= 0) {
            return;
        }
        for (Map.Entry<String, Long> entry : arrange.entrySet()) {
            totalUnExeTaskSize = redisTemplate.opsForSet().size(RedisKey.TASKS);
            if (entry.getValue() < 50 && totalUnExeTaskSize > 0) {
                List<Task> tasks = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    Task task = (Task) redisTemplate.opsForSet().pop(RedisKey.TASKS);
                    if(null == task){
                        break;
                    }
                    redisTemplate.opsForSet().add(RedisKey.TASKS_OWNER + entry.getKey(), task);
                    tasks.add(task);
                }

                if(!tasks.isEmpty()){
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setFormId(MACHINE_ID);
                    messageEvent.setToId(entry.getKey());
                    messageEvent.setType(MessageType.NEW_TASK_EVENT.getValue());
                    messageEvent.setTasks(tasks);
                    sendBroadcast(messageEvent);
                }
            }
        }
    }


    public void sendBroadcast(MessageEvent messageEvent) {
        redisTemplate.convertAndSend(RedisKey.STRATEGY_BROADCAST, messageEvent);
    }


    //启动后发送应用注册通知
    public RedisCircularizeStrategy(RedisTemplate redisTemplate, TaskContainer taskContainer) {
        this.redisTemplate = redisTemplate;
        this.taskContainer = taskContainer;

        redisTemplate.opsForSet().add(RedisKey.REGISTRY_MACHINE_LIST, MACHINE_ID);

        //key永久存储
        redisTemplate.expire(RedisKey.REGISTRY_MACHINE_LIST, Integer.MAX_VALUE, TimeUnit.DAYS);
    }

    public boolean isLeader() {
        return MACHINE_ID.equals(leaderID);
    }

    public void onMessage(MessageEvent messageEvent) {
        if (MessageType.NEW_TASK_EVENT.getValue() == messageEvent.getType() && !CollectionUtils.isEmpty(messageEvent.getTasks())) {
            for (Task task : messageEvent.getTasks()) {
                if(null == task){
                    continue;
                }
                taskContainer.acceptNewTask(task);
            }
        }
    }

}
