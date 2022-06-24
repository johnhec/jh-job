package com.jh.job.admin.model;

import lombok.Data;

@Data
public class JobInfo {
	
	private int id;				// 主键ID
	
	private int jobGroup;		// 执行器主键ID

	private String cron;			// 调度配置，值含义取决于调度类型

	private String executorHandler;		    // 执行器，任务Handler名称

	private String executorParam;		    // 执行器，任务参数

	private int triggerStatus;		// 调度状态：0-停止，1-运行

	private long triggerLastTime;	// 上次调度时间

	private long triggerNextTime;	// 下次调度时间
}
