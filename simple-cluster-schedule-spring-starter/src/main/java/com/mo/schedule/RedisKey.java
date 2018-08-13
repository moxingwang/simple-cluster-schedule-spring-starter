package com.mo.schedule;

/**
 * @description:
 * @author: MoXingwang 2018-08-12 13:52
 **/
public interface RedisKey {
    String BASE = "com:mo:simple:cluster:schedule:";
    String REGISTRY_MACHINE_LIST = BASE + "registry";
    String STRATEGY_BROADCAST = BASE + "broadcast";
    String TASKS_OWNER = BASE + "task:owner:";
    String TASKS = BASE + "tasks:";
    String LEADER = BASE + "leader";
    String FOLLOWER = BASE + "follower:";

}
