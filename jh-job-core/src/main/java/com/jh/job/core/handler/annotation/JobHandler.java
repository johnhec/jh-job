package com.jh.job.core.handler.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface JobHandler {

    /**
     * jobHandler name
     */
    String value();

}
