package com.hyf.cloudnative.remoting.mesh.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.ArrayList;
import java.util.List;

public class ApplicationUtils implements ApplicationContextAware {

    private static volatile ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static <T> List<T> getOrderedList(Class<T> clazz) {
        List<T> list = new ArrayList<>(getApplicationContext().getBeansOfType(clazz).values());
        AnnotationAwareOrderComparator.sort(list);
        return list;
    }
}
