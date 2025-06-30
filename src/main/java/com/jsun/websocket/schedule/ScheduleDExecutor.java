package com.jsun.websocket.schedule;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ScheduleDExecutor {

    private static final ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(
            1,
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy()
    );

    public static void customizedScheduledThreadPoolExecutor() throws InterruptedException {
        ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            try {
                log.info("scheduleAtFixedRate...");
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                log.error(e.getStackTrace().toString());
            }
            // 初始延时1s，每间隔5s执行一次(此处要考虑任务执行时长)
            // 间隔>任务时长，按间隔触发
            // 间隔<任务时长，按任务时长触发
        }, 1, 5, TimeUnit.SECONDS);

        while (true) {
            long delay = scheduledFuture.getDelay(TimeUnit.SECONDS);
            log.info("下次执行剩余时间={}秒", delay);
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        customizedScheduledThreadPoolExecutor();
    }
}
