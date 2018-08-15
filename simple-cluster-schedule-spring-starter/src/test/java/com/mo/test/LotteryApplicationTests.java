package com.mo.test;

import com.mo.schedule.annotation.EnableSimpleScheduleCluster;
import com.mo.schedule.config.RedisConfiguration;
import com.mo.schedule.config.ScheduledExecutorServiceConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableSimpleScheduleCluster
@Import({RedisConfiguration.class, ScheduledExecutorServiceConfiguration.class})
public class LotteryApplicationTests {

    @Test
    public void contextLoads() {

    }

}
