package com.jsun.websocket.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@EnableAsync
public class ScheduleTasks {
    @Value("${cron.enabled}")
    private boolean enabled;

    @Scheduled(cron = "${cron.scheduler}")
    public void printCurrentTimeWithCronJob() {
        if (!enabled) {
            return;
        }
        log.info("printCurrentTimeWithCronJob, current time={}", LocalDateTime.now());
    }

    // In this case, the duration between the end of the last execution and the start of the next execution is fixed. The task always waits until the previous one is finished.
    @Scheduled(fixedDelay = 1000)
    public void printCurrentTimeInFixedDelay() throws InterruptedException {
        if (!enabled) {
            return;
        }
        log.info("printCurrentTimeInFixedDelay(), current time={}", LocalDateTime.now());
        TimeUnit.SECONDS.sleep(2);
    }

    // Note that scheduled tasks don’t run in parallel by default. So even if we used fixedRate, the next task won’t be invoked until the previous one is done.
    // If you want to support parallel behavior in scheduled tasks, you need to add the @Async annotation.
    @Async
    @Scheduled(fixedRate = 5000)
    public void printCurrentTimeInFixedRate() throws InterruptedException {
        log.info("printCurrentTimeInFixedRate(), current time={}", LocalDateTime.now());
        TimeUnit.SECONDS.sleep(10);
        log.info("Task finished @{}", LocalDateTime.now());
    }
}
