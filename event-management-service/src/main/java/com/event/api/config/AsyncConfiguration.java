@Configuration
@EnableAsync
public class AsyncConfiguration {

    @Bean(name = "emailTaskExecutor")
    public Executor emailTaskExecutor() {
      
          ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
          // Core pool size - minimum number of threads
          executor.setCorePoolSize(5);
          // Max pool size - maximum number of threads
          executor.setMaxPoolSize(10);
          // Queue capacity - how many tasks can wait
          executor.setQueueCapacity(100);
          // Thread name prefix for easier debugging
          executor.setThreadNamePrefix("email-async-");
          // Kill idle threads after 60s
          executor.setKeepAliveSeconds(60);
          // Even core threads can die if idle
          executor.setAllowCoreThreadTimeOut(true);
          // What to do when queue is full
          // CallerRunsPolicy - caller thread executes the task
          executor.setRejectedExecutionHandler(
            new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()
          );
         //  GRACEFUL SHUTDOWN - Wait for tasks to complete
         executor.setWaitForTasksToCompleteOnShutdown(true);

          //  GRACEFUL SHUTDOWN - Wait up to 60 seconds
          executor.setAwaitTerminationSeconds(60);

          executor.initialize();
          return executor;
    }

    // Bean(name = "commonTaskExecutor")
    // public Executor emailTaskExecutor() {
      
    //       ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    //       executor.setCorePoolSize(5);
    //       executor.setMaxPoolSize(15);
    //       executor.setQueueCapacity(100);
    //       executor.setThreadNamePrefix("common-async-");
    //       executor.setRejectedExecutionHandler(
    //         new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()
    //       );

    //       executor.initialize();
    //       return executor;
    // }

}
