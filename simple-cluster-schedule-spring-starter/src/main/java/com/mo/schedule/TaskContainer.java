package com.mo.schedule;

import com.alibaba.fastjson.JSON;
import com.mo.schedule.entity.RedisKey;
import com.mo.schedule.entity.Task;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.mo.schedule.circularize.RedisCircularizeStrategy.MACHINE_ID;

/**
 * @description:
 * @author: MoXingwang 2018-08-11 19:08
 **/
public class TaskContainer {
    private RedisTemplate redisTemplate;
    private ApplicationContext applicationContext;
    private ArrayBlockingQueue unExeTaskQueue;
    private ExecutorService threadPool;

    public TaskContainer(RedisTemplate redisTemplate, ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.unExeTaskQueue = new ArrayBlockingQueue(1000);
        this.threadPool = new ThreadPoolExecutor(5, 10, 30, TimeUnit.MINUTES, unExeTaskQueue);
    }


    //收到新任务
    public void acceptNewTask(Task task) {
        threadPool.submit(new TaskThread(task));
    }

    protected void finishTask(Task task) {
        redisTemplate.opsForSet().remove(RedisKey.TASKS_OWNER + MACHINE_ID, task);
    }

    public class TaskThread implements Runnable {
        private Task task;
        private ScheduleClusterTask scheduleClusterTask;

        public TaskThread(Task task) {
            this.task = task;
            System.out.println("执行任务"+ JSON.toJSONString(task));

            String shortClassName = ClassUtils.getShortName(task.getTaskClassName());
            String beanName = Introspector.decapitalize(shortClassName);
            scheduleClusterTask = (ScheduleClusterTask) applicationContext.getBean(beanName);
        }

        @Override
        public void run() {
            scheduleClusterTask.start(task);
            finishTask(task);
        }
    }

    public boolean isTerminated(){
        return threadPool.isTerminated();
    }
}
