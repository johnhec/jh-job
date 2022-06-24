package com.jh.job.admin.core.trigger;

import com.jh.job.admin.conf.AdminConfig;
import com.jh.job.admin.core.scheduler.JobScheduler;
import com.jh.job.admin.model.JobGroup;
import com.jh.job.admin.model.JobInfo;
import com.jh.job.core.biz.ExecutorBiz;
import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.biz.model.TriggerParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobTrigger {
    private static Logger logger = LoggerFactory.getLogger(JobTrigger.class);

    public static void trigger(int jobId, String executorParam, String addressList) {
        JobInfo jobInfo = AdminConfig.getAdminConfig().getJobInfoDao().loadById(jobId);
        if (jobInfo == null) {
            return;
        }

        if (executorParam != null) {
            jobInfo.setExecutorParam(executorParam);
        }

        JobGroup group = AdminConfig.getAdminConfig().getJobGroupDao().load(jobInfo.getJobGroup());
        if (addressList!=null && addressList.trim().length()>0) {
            group.setAddressList(addressList.trim());
        }

        processTrigger(group, jobInfo);
    }

    private static void processTrigger(JobGroup group, JobInfo jobInfo){
        TriggerParam triggerParam = new TriggerParam();
        triggerParam.setJobId(jobInfo.getId());
        triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
        triggerParam.setExecutorParams(jobInfo.getExecutorParam());

        String address = null;
        if (group.getAddressAsList()!=null && !group.getAddressAsList().isEmpty()) {
            address = group.getAddressAsList().get(0);
        }

        if (address != null) {
            runExecutor(triggerParam, address);
        }
    }

    public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address){
        ReturnT<String> runResult = null;
        try {
            ExecutorBiz executorBiz = JobScheduler.getExecutorBiz(address);
            runResult = executorBiz.run(triggerParam);
        } catch (Exception e) {
            runResult = ReturnT.FAIL;
        }
        return runResult;
    }

}
