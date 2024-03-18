package com.itdawn.job;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
//
@Component
public class MyJob {

    @Scheduled(cron = "0 0/3 * * * ?") //在哪个方法添加了@Scheduled注解，哪个方法就会定时去执行
    public void myJob() {
        //上面那行@Scheduled注解的cron属性就是具体的定时规则。从每一分钟的0秒开始，每隔5秒钟就会执行myJob()方法
        //定时任务要执行的代码
        //System.out.println("定时任务执行了， 现在的时间为：" + LocalTime.now());
    }

}
