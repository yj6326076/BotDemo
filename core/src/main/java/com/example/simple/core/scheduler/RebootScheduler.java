package com.example.simple.core.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.restart.Restarter;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * 定时重启任务
 * @author yj632
 */
@Slf4j
@Configuration
public class RebootScheduler {

    @Scheduled(cron = "0 0 5 * * ?")
    public void reboot() {
        log.info("start to restart ,time {}", LocalDateTime.now());
        Restarter restarter = Restarter.getInstance();
        restarter.restart();
        log.info("end to restart ,time {}", LocalDateTime.now());
    }
}
