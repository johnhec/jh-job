package com.jh.job.core.contraller;

import com.jh.job.core.biz.ExecutorBiz;
import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.biz.model.TriggerParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class JobController {

	@Resource
	public ExecutorBiz executorBiz;

	@PostMapping("/run")
	public ReturnT<String> run(@RequestBody TriggerParam triggerParam){
		return executorBiz.run(triggerParam);
	}
}
