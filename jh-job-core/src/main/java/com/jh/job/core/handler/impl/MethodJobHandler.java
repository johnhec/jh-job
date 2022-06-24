package com.jh.job.core.handler.impl;

import com.jh.job.core.handler.IJobHandler;

import java.lang.reflect.Method;

public class MethodJobHandler extends IJobHandler {

    private final Object target;
    private final Method method;

    public MethodJobHandler(Object target, Method method) {
        this.target = target;
        this.method = method;
    }

    @Override
    public void execute() throws Exception {
        Class<?>[] paramTypes = method.getParameterTypes();
        if (paramTypes.length > 0) {
            method.invoke(target, new Object[paramTypes.length]);
        } else {
            method.invoke(target);
        }
    }
}
