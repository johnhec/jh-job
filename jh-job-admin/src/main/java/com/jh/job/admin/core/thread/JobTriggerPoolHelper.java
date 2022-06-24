package com.jh.job.admin.core.thread;

import com.jh.job.admin.conf.AdminConfig;
import com.jh.job.admin.core.trigger.JobTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobTriggerPoolHelper {
    private static Logger logger = LoggerFactory.getLogger(JobTriggerPoolHelper.class);

    private ThreadPoolExecutor triggerPool = null;

    public void start(){
        triggerPool = new ThreadPoolExecutor(
                10,
                AdminConfig.getAdminConfig().getTriggerPoolMax(),
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(1000),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, "triggerPool-" + r.hashCode());
                    }
                });
    }


    public void stop() {
        triggerPool.shutdownNow();
    }

    public void addTrigger(final int jobId, final String executorParam, final String addressList) {
        triggerPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    JobTrigger.trigger(jobId, executorParam, addressList);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    private static JobTriggerPoolHelper helper = new JobTriggerPoolHelper();

    public static void toStart() {
        helper.start();
    }

    public static void toStop() {
        helper.stop();
    }

    public static void trigger(int jobId, String executorParam, String addressList) {
        helper.addTrigger(jobId, executorParam, addressList);
    }

}
