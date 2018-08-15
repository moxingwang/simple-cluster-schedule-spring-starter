package com.mo.schedule.annotation;

import com.mo.schedule.config.SimpleScheduleClusterAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @description:注解启动
 * @author: MoXingwang 2018-08-11 15:32
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(SimpleScheduleClusterAutoConfiguration.class)
@Documented
public @interface EnableSimpleScheduleCluster {

}
