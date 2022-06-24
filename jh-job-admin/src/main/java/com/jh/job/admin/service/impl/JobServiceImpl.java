package com.jh.job.admin.service.impl;

import com.jh.job.admin.core.thread.JobScheduleHelper;
import com.jh.job.admin.core.thread.JobTriggerPoolHelper;
import com.jh.job.admin.dao.JobInfoDao;
import com.jh.job.admin.model.JobInfo;
import com.jh.job.admin.service.JobService;
import com.jh.job.core.biz.model.ReturnT;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class JobServiceImpl implements JobService {

    @Resource
    private JobInfoDao jobInfoDao;
    
    @Override
    public ReturnT<String> add(JobInfo jobInfo) {
        jobInfoDao.save(jobInfo);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> update(JobInfo jobInfo) {
        jobInfoDao.update(jobInfo);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> remove(int id) {
        jobInfoDao.delete(id);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<JobInfo> loadById(int id) {
        JobInfo jobInfo = jobInfoDao.loadById(id);
        return new ReturnT<>(jobInfo) ;
    }

    @Override
    public ReturnT<String> triggerJob(int id, String executorParam, String addressList) {
        if (executorParam == null) {
            executorParam = "";
        }

        JobTriggerPoolHelper.trigger(id, executorParam, addressList);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> start(int id) {
        JobInfo jobInfo = jobInfoDao.loadById(id);
        long nextTriggerTime = 0;
        try {
            Date nextValidTime = JobScheduleHelper.generateNextValidTime(jobInfo, new Date(System.currentTimeMillis()));
            if (nextValidTime == null) {
                return ReturnT.FAIL;
            }
            nextTriggerTime = nextValidTime.getTime();
        } catch (Exception e) {
            return ReturnT.FAIL;
        }

        jobInfo.setTriggerStatus(1);
        jobInfo.setTriggerLastTime(0);
        jobInfo.setTriggerNextTime(nextTriggerTime);

        jobInfoDao.update(jobInfo);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> stop(int id) {
        JobInfo jobInfo = jobInfoDao.loadById(id);

        jobInfo.setTriggerStatus(0);
        jobInfo.setTriggerLastTime(0);
        jobInfo.setTriggerNextTime(0);

        jobInfoDao.update(jobInfo);
        return ReturnT.SUCCESS;
    }
}
