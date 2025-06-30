### Reference - Learning only

https://gitee.com/wangshisuifeng123/web-scoket/tree/master/webscoketService

### Spring Boot Scheduling

#### Timer类， JDK1.3引入的，不推荐

#### Spring @Scheduled注解，不是很推荐。

这种方式底层虽然是用线程池实现，但是有个最大的问题，`所有的任务`都使用`同一个线程池`
，可能会导致长周期的任务运行影响短周期任务运行，造成线程池"饥饿。更加推荐的做法是同种类型的任务使用同一个线程池。

1. Enable `@EnableScheduling`
   in [WebSocketServerApplication.java](src/main/java/com/jsun/websocket/WebSocketServerApplication.java)
2. Create [ScheduleTasks.java](src/main/java/com/jsun/websocket/schedule/ScheduleTasks.java) file
3. Add tasks

- @Scheduled(cron = "${cron.scheduler}") public void printCurrentTimeWithCronJob()
- @Scheduled(fixedDelay = 1000) public void printCurrentTimeInFixedDelay()
- @Async
  @Scheduled(fixedRate = 5000) public void printCurrentTimeInFixedRate()

4. Enable `@EnableAsync` for ScheduleTasks.java
5. Add switch to on / off schedulers
6. Specify scheduling thread pool size in application.yaml file

#### 自定义ScheduledThreadPoolExecutor实现调度任务

通过自定义ScheduledThreadPoolExecutor调度线程池，提交调度任务才是最优解。

示例代码： 
```java
private static final ScheduledThreadPoolExecutor scheduledExecutorService = new ScheduledThreadPoolExecutor(
        3,
        Executors.defaultThreadFactory(),
        new ThreadPoolExecutor.DiscardOldestPolicy()
);

public static void customizedScheduledThreadPoolExecutor() {
    scheduledExecutorService.scheduleAtFixedRate(() -> {
        try {
            log.info("scheduleAtFixedRate...");
            TimeUnit.SECONDS.sleep(7);
        } catch (InterruptedException e) {
            log.error(e.getStackTrace().toString());
        }
        // 无初始延时，每间隔5s执行一次(此处要考虑任务执行时长)
        // 间隔>任务时长，按间隔触发
        // 间隔<任务时长，按任务时长触发
    }, 0, 5, TimeUnit.SECONDS);
}
```

调用调用getDelay()可以获取任务下次的执行时间点，非常好用的。
```java
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
```
使用ScheduledThreadPoolExecutor时`一定要注意异常处理`， 如果使用不当，会导致定时任务不再执行，***记住要try catch捕获异常 !!!***