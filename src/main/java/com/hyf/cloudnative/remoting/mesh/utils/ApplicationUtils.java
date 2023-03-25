package com.hyf.cloudnative.remoting.mesh.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * spring application util.
 */
public class ApplicationUtils implements ApplicationContextAware {

    private static volatile ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ApplicationUtils.applicationContext = applicationContext;
    }

    public static <T> List<T> getOrderedList(Class<T> clazz) {
        List<T> list = new ArrayList<>(getApplicationContext().getBeansOfType(clazz).values());
        AnnotationAwareOrderComparator.sort(list);
        return list;
    }
}
