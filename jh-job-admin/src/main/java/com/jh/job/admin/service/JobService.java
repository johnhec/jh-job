package com.jh.job.admin.service;

import com.jh.job.admin.model.JobInfo;
import com.jh.job.core.biz.model.ReturnT;

public interface JobService {

    public ReturnT<String> add(JobInfo jobInfo);

    public ReturnT<String> update(JobInfo jobInfo);

    public ReturnT<String> remove(int id);

    public ReturnT<JobInfo> loadById(int id);

    public ReturnT<String> triggerJob(int id, String executorParam, String addressList);

    public ReturnT<String> start(int id);

    public ReturnT<String> stop(int id);
}
