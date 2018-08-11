package com.mo.schedule.circularize;

import com.mo.schedule.entity.MessageEvent;

/**
 * @description: 集群之间的通知
 * @author: MoXingwang 2018-08-11 15:18
 **/
@FunctionalInterface
public interface CircularizeStrategy {
    int MESSAGE_TYPE_SEND = 0;

    /**
     * 发送通知
     * @param messageEvent
     */
    void sendMessage(MessageEvent messageEvent);


    //发送心跳广播

    //接收到心跳广播

    //发送新任务

    //收到新任务

    //发送撤回任务通知

    //收到撤回任务通知

    //发送撤回本地任务

    //收到撤回本地任务{master收到才会处理}

    //发布新的任务列表

    //收到新的任务列表{master收到才会处理}

    //master定时巡检和重新编排任务

}
