package com.jh.job.admin.conf;

import com.jh.job.admin.core.scheduler.JobScheduler;
import com.jh.job.admin.dao.JobGroupDao;
import com.jh.job.admin.dao.JobInfoDao;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class AdminConfig implements InitializingBean, DisposableBean {

    private static AdminConfig adminConfig = null;

    public static AdminConfig getAdminConfig() {
        return adminConfig;
    }

    private JobScheduler jobScheduler;

    @Override
    public void afterPropertiesSet() {
        adminConfig = this;
        jobScheduler = new JobScheduler();
        jobScheduler.init();
    }

    @Override
    public void destroy() {
        jobScheduler.destroy();
    }

    // dao, service
    @Resource
    private JobInfoDao jobInfoDao;

    @Resource
    private JobGroupDao jobGroupDao;

    public JobInfoDao getJobInfoDao() {
        return jobInfoDao;
    }

    public JobGroupDao getJobGroupDao() {
        return jobGroupDao;
    }

    // conf
    @Value("${xxl.job.triggerpool.max}")
    private int triggerPoolMax;

    public int getTriggerPoolMax() {
        if (triggerPoolMax < 200) {
            return 200;
        }
        return triggerPoolMax;
    }

}
