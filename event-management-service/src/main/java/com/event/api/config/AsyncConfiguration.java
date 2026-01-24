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
          // What to do when queue is full
          // CallerRunsPolicy - caller thread executes the task
          executor.setRejectedExecutionHandler(
            new java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy()
          );
          executor.initialize();
          return executor;
    }

}
