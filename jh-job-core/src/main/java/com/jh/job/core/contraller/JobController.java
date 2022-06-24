package com.jh.job.core.contraller;

import com.jh.job.core.biz.ExecutorBiz;
import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.biz.model.TriggerParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class JobController {

	@Resource
	public ExecutorBiz executorBiz;

	@PostMapping("/run")
	public ReturnT<String> run(@RequestBody TriggerParam triggerParam){
		return executorBiz.run(triggerParam);
	}

	@GetMapping("/hello/{id}")
	public ReturnT<String> hello(@PathVariable int id){
		return new ReturnT("hello" + id);
	}
}
