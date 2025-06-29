### Reference - Learning only

https://gitee.com/wangshisuifeng123/web-scoket/tree/master/webscoketService

### Spring Boot Scheduling

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
