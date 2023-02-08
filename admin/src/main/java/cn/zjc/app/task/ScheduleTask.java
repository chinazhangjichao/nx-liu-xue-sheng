package cn.zjc.app.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

/**
 * @author ZJC
 * @decription:
 * @date: 2022-08-24 14:58
 * @since JDK 1.8
 */
@EnableAsync
@Component
@EnableScheduling
public class ScheduleTask {

    private static Calendar calendar = Calendar.getInstance();
    //每月1号00:00:00检查是否添加新的表分区
    @Async
    @Scheduled(cron="0 0 0 1 * ?")
    public void partitionCheck(){

    }








}
