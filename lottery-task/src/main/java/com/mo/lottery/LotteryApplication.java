package com.mo.lottery;

import com.mo.schedule.RedisCircularizeStrategyConfiguration;
import com.mo.schedule.annotation.EnableSimpleScheduleCluster;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableSimpleScheduleCluster
@Import(RedisCircularizeStrategyConfiguration.class)
public class LotteryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LotteryApplication.class, args);
    }

}
