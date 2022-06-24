package com.jh.job.core.example;

import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TestJob {

    private static final Logger logger = LoggerFactory.getLogger(TestJob.class);

    @JobHandler("test")
    public ReturnT<String> execute(String param) throws Exception {
        logger.info("Test Job handler execute");
        return ReturnT.SUCCESS;
    }

}
