package com.mo.lottery;

import com.mo.schedule.annotation.EnableSimpleScheduleCluster;
import com.mo.schedule.config.RedisConfiguration;
import com.mo.schedule.config.ScheduledExecutorServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableSimpleScheduleCluster
@Import({RedisConfiguration.class,ScheduledExecutorServiceConfiguration.class})
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }

}
