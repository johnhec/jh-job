package com.jh.job.admin.controller;

import com.jh.job.admin.model.JobGroup;
import com.jh.job.admin.dao.JobGroupDao;
import com.jh.job.core.biz.model.ReturnT;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/jobgroup")
public class JobGroupController {

	@Resource
	public JobGroupDao jobGroupDao;

	/**
	 * 创建执行器
	 * @param jobGroup
	 * @return
	 */
	@PostMapping("/save")
	public ReturnT<String> save(@RequestBody JobGroup jobGroup){
		jobGroupDao.save(jobGroup);
		return ReturnT.SUCCESS;
	}

	/**
	 * 查看执行器
	 * @param id
	 * @return
	 */
	@PostMapping("/loadById")
	public ReturnT<JobGroup> loadById(int id){
		JobGroup jobGroup = jobGroupDao.load(id);
		return new ReturnT<JobGroup>(jobGroup);
	}

}
