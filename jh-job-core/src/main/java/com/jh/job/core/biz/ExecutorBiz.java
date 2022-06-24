package com.jh.job.core.biz;

import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.biz.model.TriggerParam;

public interface ExecutorBiz {

    public ReturnT<String> run(TriggerParam triggerParam);

}
