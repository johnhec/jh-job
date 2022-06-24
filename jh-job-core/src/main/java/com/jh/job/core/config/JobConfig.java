package com.jh.job.core.config;

import com.jh.job.core.executor.impl.JobSpringExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JobConfig {

    @Bean
    public JobSpringExecutor jobExecutor() {
        JobSpringExecutor jobSpringExecutor = new JobSpringExecutor();
        return jobSpringExecutor;
    }
}