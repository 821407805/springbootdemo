package com.yijiang.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @ClassName: ScheduleService
 * @Description: 定时任务
 * @Author Jason
 * @Date 2020/9/3 9:26
 */
@Service
public class ScheduleService {
    @Scheduled(cron="0/5 * * * * *")
    public void scheduleTest(){
        System.out.printf("当前时间是：%s \n", LocalDateTime.now());
    }
}
