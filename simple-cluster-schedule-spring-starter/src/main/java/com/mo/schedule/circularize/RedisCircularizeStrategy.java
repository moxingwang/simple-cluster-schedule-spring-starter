package com.mo.schedule.circularize;

import com.alibaba.fastjson.JSON;
import com.mo.schedule.TaskContainer;
import com.mo.schedule.entity.MessageEvent;
import com.mo.schedule.entity.MessageType;
import com.mo.schedule.entity.RedisKey;
import com.mo.schedule.entity.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(RedisCircularizeStrategy.class);

    //每个应用启动后的唯一标识
    public static final String MACHINE_ID = UUID.randomUUID().toString();

    private RedisTemplate redisTemplate;
    private TaskContainer taskContainer;

    /**
     * 本地leader缓存
     */
    private String leaderID = null;

    /**
     * 心跳
     */
    @Scheduled(fixedRate = 5000)
    public void leaderHeartbeat() {
        //心跳维持
        if (isLeader()) {
            redisTemplate.opsForValue().set(RedisKey.LEADER, MACHINE_ID, 10, TimeUnit.SECONDS);
        } else {
            redisTemplate.opsForValue().set(RedisKey.FOLLOWER + MACHINE_ID, "1", 10, TimeUnit.SECONDS);
        }

        if (isLeader()) {
            //检查follower心跳
            Set<String> machines = redisTemplate.opsForSet().members(RedisKey.REGISTRY_MACHINE_LIST);

            //检查machines的心跳
            for (String machine : machines) {
                if (!MACHINE_ID.equals(machine)) {
                    Object machineObj = redisTemplate.opsForValue().get(RedisKey.FOLLOWER + machine);
                    if (null == machineObj) {
                        //清除这台机器并且收回所有任务
                        redisTemplate.opsForSet().unionAndStore(RedisKey.TASKS, RedisKey.TASKS_OWNER + machine, RedisKey.TASKS);
                        redisTemplate.opsForSet().remove(RedisKey.REGISTRY_MACHINE_LIST, machine);
                        redisTemplate.delete(RedisKey.TASKS_OWNER + machine);
                    }
                }
            }
        } else {
            //维持心跳列表
            redisTemplate.opsForSet().add(RedisKey.REGISTRY_MACHINE_LIST, MACHINE_ID);

            //检测leader的存在性
            Object leader = redisTemplate.opsForValue().get(RedisKey.LEADER);
            if (leader == null) {
                //leader已不存在,自己竞选leader
                boolean success = redisTemplate.opsForValue().setIfAbsent(RedisKey.LEADER, MACHINE_ID);
                if (success) {
                    redisTemplate.opsForValue().set(RedisKey.LEADER, MACHINE_ID, 10, TimeUnit.SECONDS);
                    leaderID = MACHINE_ID;
                }
            }
        }
    }

    /**
     * 编排任务
     */
    @Scheduled(fixedRate = 5000)
    public void taskPolling() {
        if (!isLeader()) {
            return;
        }
        Set<String> machines = redisTemplate.opsForSet().members(RedisKey.REGISTRY_MACHINE_LIST);
        Map<String, Long> arrange = new HashMap<>();

        for (String machine : machines) {
            arrange.put(machine, redisTemplate.opsForSet().size(RedisKey.TASKS_OWNER + machine));
        }

        arrangeTasks(arrange);
    }

    private void arrangeTasks(Map<String, Long> arrange) {
        Long totalUnExeTaskSize = redisTemplate.opsForSet().size(RedisKey.TASKS);
        logger.info("总任务数量{};机器{}", totalUnExeTaskSize, JSON.toJSONString(arrange));
        if (totalUnExeTaskSize <= 0) {
            return;
        }
        for (Map.Entry<String, Long> entry : arrange.entrySet()) {
            totalUnExeTaskSize = redisTemplate.opsForSet().size(RedisKey.TASKS);
            if (totalUnExeTaskSize <= 0) {
                return;
            }
            if (entry.getValue() < 300) {
                List<Task> tasks = new ArrayList<>();
                for (int i = 0; i < 300 - entry.getValue(); i++) {
                    Task task = (Task) redisTemplate.opsForSet().pop(RedisKey.TASKS);
                    if (null == task) {
                        break;
                    }
                    redisTemplate.opsForSet().add(RedisKey.TASKS_OWNER + entry.getKey(), task);
                    tasks.add(task);
                }

                if (!tasks.isEmpty()) {
                    MessageEvent messageEvent = new MessageEvent();
                    messageEvent.setToId(entry.getKey());
                    messageEvent.setType(MessageType.NEW_TASK_EVENT.getValue());
                    messageEvent.setTasks(tasks);
                    sendBroadcast(messageEvent);
                }
            } else {
                //检查本地任务并且同步
                Long redisTaskCount = redisTemplate.opsForSet().size(RedisKey.TASKS_OWNER + entry.getKey());
                int localTaskCount = taskContainer.localTaskCount();

                logger.info("远程任务{}本地{}", redisTaskCount, localTaskCount);

                if (redisTaskCount - localTaskCount > 50) {
                    Set<Task> tasks = redisTemplate.opsForSet().members(RedisKey.TASKS_OWNER + entry.getKey());
                    for (Task task : tasks) {
                        taskContainer.acceptNewTask(task);
                    }
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

        redisTemplate.opsForValue().set(RedisKey.FOLLOWER + MACHINE_ID, "1", 10, TimeUnit.SECONDS);
        redisTemplate.opsForSet().add(RedisKey.REGISTRY_MACHINE_LIST, MACHINE_ID);
        redisTemplate.expire(RedisKey.REGISTRY_MACHINE_LIST, 30, TimeUnit.DAYS);
        redisTemplate.expire(RedisKey.TASKS_OWNER + MACHINE_ID, 10, TimeUnit.DAYS);
    }

    public boolean isLeader() {
        return MACHINE_ID.equals(leaderID);
    }

    public void onMessage(MessageEvent messageEvent) {
        if (MessageType.NEW_TASK_EVENT.getValue() == messageEvent.getType() && !CollectionUtils.isEmpty(messageEvent.getTasks())) {
            for (Task task : messageEvent.getTasks()) {
                if (null == task) {
                    continue;
                }
                taskContainer.acceptNewTask(task);
            }
        }
    }

}
