package com.chiblinx.core.config;

import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableAsync
public class AsyncConfiguration {

  private static final int AVAILABLE_CPU_CORES = Runtime.getRuntime().availableProcessors();

  @Primary
  @Bean(name = "taskExecutorDefault")
  TaskExecutor taskExecutorDefault() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(getCorePoolSize(2));
    executor.setMaxPoolSize(AVAILABLE_CPU_CORES);
    executor.setQueueCapacity(500);
    executor.setThreadNamePrefix("Async-Default-");
    executor.setRejectedExecutionHandler((r, e) -> {
      log.warn("Task rejected, Thread pool is full and queue is also full. Task: [{}]",
          r.toString());
      new ThreadPoolExecutor.CallerRunsPolicy();
    });
    executor.initialize();

    return executor;
  }

  private int getCorePoolSize(int min) {
    return Math.max(AVAILABLE_CPU_CORES - min, 1);
  }

}
