package com.jh.job.admin.client;

import com.jh.job.admin.util.HttpUtil;
import com.jh.job.core.biz.ExecutorBiz;
import com.jh.job.core.biz.model.ReturnT;
import com.jh.job.core.biz.model.TriggerParam;

public class ExecutorBizClient implements ExecutorBiz {
    private String addressUrl ;

    private int timeout = 3;

    public ExecutorBizClient(String addressUrl) {
        this.addressUrl = addressUrl;

        if (!this.addressUrl.endsWith("/")) {
            this.addressUrl = this.addressUrl + "/";
        }
    }

    @Override
    public ReturnT<String> run(TriggerParam triggerParam) {
        return HttpUtil.postBody(addressUrl + "job-core/run", timeout, triggerParam, String.class);
    }

}
