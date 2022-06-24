package com.jh.job.core.executor;

import com.jh.job.core.handler.IJobHandler;
import com.jh.job.core.handler.annotation.JobHandler;
import com.jh.job.core.handler.impl.MethodJobHandler;
import com.jh.job.core.thread.JobThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class JobExecutor {
    private static final Logger logger = LoggerFactory.getLogger(JobExecutor.class);

//    // ---------------------- start + stop ----------------------
//    public void start() throws Exception {
//    }

    public void destroy(){
        if (jobThreadRepository.size() > 0) {
            for (Map.Entry<Integer, JobThread> item: jobThreadRepository.entrySet()) {
                JobThread oldJobThread = removeJobThread(item.getKey());
                if (oldJobThread != null) {
                    try {
                        oldJobThread.join();
                    } catch (InterruptedException e) {
                        logger.error("JobThread destroy(join) error, jobId:{}", item.getKey(), e);
                    }
                }
            }
            jobThreadRepository.clear();
        }
        jobHandlerRepository.clear();
    }

    // ---------------------- job handler manage ----------------------
    private static ConcurrentMap<String, IJobHandler> jobHandlerRepository = new ConcurrentHashMap<String, IJobHandler>();

    public static IJobHandler loadJobHandler(String name){
        return jobHandlerRepository.get(name);
    }

    public static IJobHandler registerJobHandler(String name, IJobHandler jobHandler){
        logger.info("register jobHandler success, name:{}, jobHandler:{}", name, jobHandler);
        return jobHandlerRepository.put(name, jobHandler);
    }

    protected void registerJobHandler(JobHandler jobHandler, Object bean, Method executeMethod){
        if (jobHandler == null) {
            return;
        }

        String name = jobHandler.value();
        String methodName = executeMethod.getName();
        if (name.trim().length() == 0) {
            throw new RuntimeException("jobHandler name invalid, for[" + bean.getClass() + "#" + methodName + "] .");
        }
        if (loadJobHandler(name) != null) {
            throw new RuntimeException("jobHandler[" + name + "] naming conflicts.");
        }

        executeMethod.setAccessible(true);

        registerJobHandler(name, new MethodJobHandler(bean, executeMethod));

    }

    // ---------------------- job thread manage ----------------------
    private static ConcurrentMap<Integer, JobThread> jobThreadRepository = new ConcurrentHashMap<Integer, JobThread>();

    public static JobThread registerJobThread(int jobId, IJobHandler handler){
        JobThread newJobThread = new JobThread(jobId, handler);
        newJobThread.start();

        JobThread oldJobThread = jobThreadRepository.put(jobId, newJobThread);
        if (oldJobThread != null) {
            oldJobThread.toStop();
            oldJobThread.interrupt();
        }

        return newJobThread;
    }

    public static JobThread removeJobThread(int jobId){
        JobThread oldJobThread = jobThreadRepository.remove(jobId);
        if (oldJobThread != null) {
            oldJobThread.toStop();
            oldJobThread.interrupt();

            return oldJobThread;
        }
        return null;
    }

    public static JobThread loadJobThread(int jobId){
        return jobThreadRepository.get(jobId);
    }
}
