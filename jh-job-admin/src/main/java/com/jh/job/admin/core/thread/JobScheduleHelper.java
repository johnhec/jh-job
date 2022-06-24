package com.jh.job.admin.core.thread;

import com.jh.job.admin.conf.AdminConfig;
import com.jh.job.admin.core.cron.CronExpression;
import com.jh.job.admin.model.JobInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JobScheduleHelper {
    private static Logger logger = LoggerFactory.getLogger(JobScheduleHelper.class);

    private static JobScheduleHelper instance = new JobScheduleHelper();
    public static JobScheduleHelper getInstance(){
        return instance;
    }

    private Thread scheduleThread;
    private volatile boolean scheduleThreadToStop = false;

    public void start(){
        scheduleThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                while (!scheduleThreadToStop) {
                    try {
                        long nowTime = System.currentTimeMillis();
                        List<JobInfo> scheduleList = AdminConfig.getAdminConfig().getJobInfoDao().scheduleJobQuery(nowTime);
                        if (scheduleList!=null && scheduleList.size()>0) {
                            for (JobInfo jobInfo: scheduleList) {
                                JobTriggerPoolHelper.trigger(jobInfo.getId(), jobInfo.getExecutorParam(), null);
                                refreshNextValidTime(jobInfo, new Date());
                            }
                            for (JobInfo jobInfo: scheduleList) {
                                AdminConfig.getAdminConfig().getJobInfoDao().scheduleUpdate(jobInfo);
                            }

                        }
                    } catch (Exception e) {
                        if (!scheduleThreadToStop) {
                            logger.error("scheduleThread error:{}", e);
                        }
                    }

                }

                logger.info("scheduleThread stop");
            }
        });
        scheduleThread.setDaemon(true);
        scheduleThread.setName("scheduleThread");
        scheduleThread.start();
    }

    public void toStop() {
        scheduleThreadToStop = true;
        try {
            TimeUnit.SECONDS.sleep(1);  // wait
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
        if (scheduleThread.getState() != Thread.State.TERMINATED){
            scheduleThread.interrupt();
            try {
                scheduleThread.join();
            } catch (InterruptedException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void refreshNextValidTime(JobInfo jobInfo, Date fromTime) throws Exception {
        Date nextValidTime = generateNextValidTime(jobInfo, fromTime);
        if (nextValidTime != null) {
            jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
            jobInfo.setTriggerNextTime(nextValidTime.getTime());
        } else {
            jobInfo.setTriggerStatus(0);
            jobInfo.setTriggerLastTime(0);
            jobInfo.setTriggerNextTime(0);
        }
    }

    public static Date generateNextValidTime(JobInfo jobInfo, Date fromTime) throws Exception {
        Date nextValidTime = new CronExpression(jobInfo.getCron()).getNextValidTimeAfter(fromTime);
        return nextValidTime;
    }

}
