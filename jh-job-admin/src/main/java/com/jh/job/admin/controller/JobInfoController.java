package com.jh.job.admin.controller;

import com.jh.job.admin.model.JobInfo;
import com.jh.job.admin.service.JobService;
import com.jh.job.core.biz.model.ReturnT;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/jobinfo")
public class JobInfoController {

	@Resource
	private JobService jobService;

	/**
	 * 创建任务
	 * @param jobInfo
	 * @return
	 */
	@PostMapping("/add")
	public ReturnT<String> add(@RequestBody JobInfo jobInfo) {
		return jobService.add(jobInfo);
	}

	/**
	 * 更新任务
	 * @param jobInfo
	 * @return
	 */
	@PostMapping("/update")
	public ReturnT<String> update(@RequestBody JobInfo jobInfo) {
		return jobService.update(jobInfo);
	}

	/**
	 * 删除任务
	 * @param id
	 * @return
	 */
	@PostMapping("/remove")
	public ReturnT<String> remove(int id) {
		return jobService.remove(id);
	}

	/**
	 * 查询任务
	 * @param id
	 * @return
	 */
	@PostMapping("/loadById")
	public ReturnT<JobInfo> loadById(int id){
		return jobService.loadById(id);
	}

	/**
	 * 执行任务
	 * @param id
	 * @param executorParam
	 * @param addressList
	 * @return
	 */
	@PostMapping("/trigger")
	public ReturnT<String> triggerJob(int id, String executorParam, String addressList) {
		return jobService.triggerJob(id, executorParam, addressList);
	}

	/**
	 * 启动任务
	 * @param id
	 * @return
	 */
	@PostMapping("/start")
	public ReturnT<String> start(int id) {
		return jobService.start(id);
	}

	/**
	 * 停止任务
	 * @param id
	 * @return
	 */
	@PostMapping("/stop")
	public ReturnT<String> pause(int id) {
		return jobService.stop(id);
	}

}
