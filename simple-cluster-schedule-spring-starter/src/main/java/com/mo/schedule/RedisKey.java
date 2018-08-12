package com.mo.schedule;

/**
 * @description:
 * @author: MoXingwang 2018-08-12 13:52
 **/
public interface RedisKey {
    String BASE = "com:mo:simple:cluster:schedule:";
    String HEARTBEAT_REGISTRY_REDIS_KEY = BASE + "heartbeat:registry";
    String STRATEGY_REDIS_KEY = BASE + "broadcast";
    String TASKS_KEY = BASE + "tasks";

}
