package com.jh.job.core.biz.impl;

import com.jh.job.core.biz.ExecutorBiz;
import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.biz.model.TriggerParam;
import com.jh.job.core.executor.JobExecutor;
import com.jh.job.core.handler.IJobHandler;
import com.jh.job.core.thread.JobThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecutorBizImpl implements ExecutorBiz {
    private static Logger logger = LoggerFactory.getLogger(ExecutorBizImpl.class);

    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        // load old jobThread
        JobThread jobThread = JobExecutor.loadJobThread(triggerParam.getJobId());
        IJobHandler jobHandler = jobThread!=null?jobThread.getHandler():null;

        // load new jobThread
        IJobHandler newJobHandler = JobExecutor.loadJobHandler(triggerParam.getExecutorHandler());

        if (jobThread!=null && jobHandler != newJobHandler) {
            jobThread = null;
            jobHandler = null;
        }

        if (jobHandler == null) {
            jobHandler = newJobHandler;
            if (jobHandler == null) {
                return new ReturnT<String>(ReturnT.FAIL_CODE, "job handler [" + triggerParam.getExecutorHandler() + "] not found.");
            }
        }
        if (jobThread == null) {
            jobThread = JobExecutor.registerJobThread(triggerParam.getJobId(), jobHandler);
        }

        // push data to queue
        ReturnT<String> pushResult = jobThread.pushTriggerQueue(triggerParam);
        return pushResult;
    }

}
