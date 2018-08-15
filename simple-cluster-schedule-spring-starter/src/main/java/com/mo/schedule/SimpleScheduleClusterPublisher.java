package com.mo.schedule;

import com.mo.schedule.entity.RedisKey;
import com.mo.schedule.entity.Task;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:
 * @author: MoXingwang 2018-08-13 13:20
 **/
public class SimpleScheduleClusterPublisher {
    private RedisTemplate redisTemplate;
    private String packageName;
    private static final Map<String, Set<String>> classContainer = new ConcurrentHashMap<>(16);

    public SimpleScheduleClusterPublisher(RedisTemplate redisTemplate, String packageName) {
        this.redisTemplate = redisTemplate;
        this.packageName = packageName;
    }

    /**
     * 发布任务
     *
     * @param tasks
     */
    public void publishTask(List<Task> tasks) {
        for (String s : listSubServiceClass(packageName)) {
            publishTask(tasks, s);
        }
    }

    /**
     * 发布任务
     *
     * @param tasks
     * @param className
     */
    public void publishTask(List<Task> tasks, String className) {
        for (Task task : tasks) {
            task.setTaskClassName(className);
            task.setTaskName("name");
            redisTemplate.opsForSet().add(RedisKey.TASKS, task);
        }
    }


    private Set<String> listSubServiceClass(String packageName) {
        Set<String> allClasses = null;
        if ((allClasses = classContainer.get(packageName)) != null) {
            return allClasses;
        }
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(true));
        allClasses = reflections.getStore().getSubTypesOf(ScheduleClusterTask.class.getName());
        classContainer.put(packageName, allClasses);

        return allClasses;
    }
}
