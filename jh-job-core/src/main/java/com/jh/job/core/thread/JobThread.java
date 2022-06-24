package com.jh.job.core.thread;

import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.biz.model.TriggerParam;
import com.jh.job.core.executor.JobExecutor;
import com.jh.job.core.handler.IJobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class JobThread extends Thread{
	private static Logger logger = LoggerFactory.getLogger(JobThread.class);

	private int jobId;
	private IJobHandler handler;
	private LinkedBlockingQueue<TriggerParam> triggerQueue;

	private volatile boolean toStop = false;

    private boolean running = false;    // if running job
	private int idleTimes = 0;			// idel times


	public JobThread(int jobId, IJobHandler handler) {
		this.jobId = jobId;
		this.handler = handler;
		this.triggerQueue = new LinkedBlockingQueue<TriggerParam>();

		this.setName("JobThread-"+jobId+"-"+System.currentTimeMillis());
	}
	public IJobHandler getHandler() {
		return handler;
	}

	public ReturnT<String> pushTriggerQueue(TriggerParam triggerParam) {
		triggerQueue.add(triggerParam);
        return ReturnT.SUCCESS;
	}

	public void toStop() {
		this.toStop = true;
	}

    @Override
	public void run() {
		while(!toStop){
			running = false;
			idleTimes++;
            TriggerParam triggerParam = null;
            try {
				triggerParam = triggerQueue.poll(3L, TimeUnit.SECONDS);
				if (triggerParam!=null) {
					running = true;
					idleTimes = 0;

					handler.execute();

				} else {
					if (idleTimes > 30) {
						if(triggerQueue.size() == 0) {
							JobExecutor.removeJobThread(jobId);
						}
					}
				}
			} catch (Throwable e) {
				logger.error("job thread {}", e);
            }
        }
	}
}
