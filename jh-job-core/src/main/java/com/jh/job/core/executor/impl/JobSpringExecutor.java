package com.jh.job.core.executor.impl;

import com.jh.job.core.executor.JobExecutor;
import com.jh.job.core.handler.annotation.JobHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Map;

public class JobSpringExecutor extends JobExecutor implements ApplicationContextAware, SmartInitializingSingleton, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(JobSpringExecutor.class);

    // start
    @Override
    public void afterSingletonsInstantiated() {
        // init JobHandler Repository (for method)
        initJobHandlerMethodRepository(applicationContext);

//        // super start
//        try {
//            super.start();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    // destroy
    @Override
    public void destroy() {
        super.destroy();
    }

    private void initJobHandlerMethodRepository(ApplicationContext applicationContext) {
        if (applicationContext == null) {
            return;
        }
        // init job handler from method
        String[] beanDefinitionNames = applicationContext.getBeanNamesForType(Object.class, false, true);
        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = applicationContext.getBean(beanDefinitionName);

            Map<Method, JobHandler> annotatedMethods = null;
            try {
                annotatedMethods = MethodIntrospector.selectMethods(bean.getClass(),
                        new MethodIntrospector.MetadataLookup<JobHandler>() {
                            @Override
                            public JobHandler inspect(Method method) {
                                return AnnotatedElementUtils.findMergedAnnotation(method, JobHandler.class);
                            }
                        });
            } catch (Throwable ex) {
                logger.error("method jobHandler resolve error for bean[" + beanDefinitionName + "].", ex);
            }
            if (annotatedMethods==null || annotatedMethods.isEmpty()) {
                continue;
            }

            for (Map.Entry<Method, JobHandler> methodXxlJobEntry : annotatedMethods.entrySet()) {
                Method executeMethod = methodXxlJobEntry.getKey();
                JobHandler jobHandler = methodXxlJobEntry.getValue();
                registerJobHandler(jobHandler, bean, executeMethod);
            }
        }
    }

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        JobSpringExecutor.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
