package com.mo.schedule;

import com.mo.schedule.entity.RedisKey;
import com.mo.schedule.entity.Task;
import com.mo.schedule.entity.TaskStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

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
        try {
            unExeTaskQueue.add(new TaskThread(task));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void startTask(Task task) {
        task.setTaskStatus(TaskStatus.EXECUTING.getValue());
        redisTemplate.opsForSet().add(RedisKey.TASKS_OWNER + MACHINE_ID, task);
    }

    protected void finishTask(Task task) {
        redisTemplate.opsForSet().remove(RedisKey.TASKS_OWNER + MACHINE_ID, task);
    }

    public class TaskThread implements Runnable {
        private Task task;
        ScheduleClusterTask scheduleClusterTask;

        public TaskThread(Task task) throws ClassNotFoundException {
            scheduleClusterTask = (ScheduleClusterTask) applicationContext.getBean(Class.forName(task.getTaskClassName()));
            this.task = task;
        }

        @Override
        public void run() {
            startTask(task);
            scheduleClusterTask.start(task);
            finishTask(task);
        }
    }
}
