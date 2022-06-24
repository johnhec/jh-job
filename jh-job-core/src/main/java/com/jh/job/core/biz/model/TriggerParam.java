package com.jh.job.core.biz.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class TriggerParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private int jobId;

    private String executorHandler;

    private String executorParams;
}
