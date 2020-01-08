package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @author cenkang
 * @Date 2020/1/8 17:25
 */
@Configuration
@EnableAsync // 开启异步配置
public class AsyncConfig {
    @Bean
    // 重写AsyncTaskExecutor,配置线程参数
    AsyncTaskExecutor asyncTaskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(100);
        executor.setQueueCapacity(25);
        executor.setMaxPoolSize(500);
        return executor;
    }
}
