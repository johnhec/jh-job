package com.jh.job.admin.core.scheduler;

import com.jh.job.admin.client.ExecutorBizClient;
import com.jh.job.admin.core.thread.JobScheduleHelper;
import com.jh.job.admin.core.thread.JobTriggerPoolHelper;
import com.jh.job.core.biz.ExecutorBiz;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JobScheduler {

    public void init() {
        JobTriggerPoolHelper.toStart();
        JobScheduleHelper.getInstance().start();
    }

    public void destroy() {
        JobScheduleHelper.getInstance().toStop();
        JobTriggerPoolHelper.toStop();
    }

    private static ConcurrentMap<String, ExecutorBiz> executorBizRepository = new ConcurrentHashMap<String, ExecutorBiz>();

    public static ExecutorBiz getExecutorBiz(String address)  {
        if (address==null || address.trim().length()==0) {
            return null;
        }

        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        executorBiz = new ExecutorBizClient(address);

        executorBizRepository.put(address, executorBiz);
        return executorBiz;
    }

}
