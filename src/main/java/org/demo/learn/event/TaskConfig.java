package org.demo.learn.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.*;

/**
 * @author luwt-a
 * @date 2022/7/16
 */
@EnableAsync
@Configuration
public class TaskConfig {

    /**
     * 服务器核数
     */
    private static final int PROCESSOR_SIZE = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = PROCESSOR_SIZE << 1;

    private static final int MAX_POOL_SIZE = PROCESSOR_SIZE << 3;

    private static final long KEEP_ALIVE_TIME = 10L;

    private static final int QUEUE_CAPACITY = 1000;

    /**
     * 提供给 Async 注解使用的线程池
     */
    @Primary
    @Bean
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setKeepAliveSeconds(20);
        executor.setQueueCapacity(10);
        executor.setThreadNamePrefix("test thread-----");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }

    /**
     * 配置自定义线程池
     */
    @Bean
    public ExecutorService executorService() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE,
                KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
