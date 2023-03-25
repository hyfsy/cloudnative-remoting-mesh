package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.GrpcController;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.server.GrpcServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;
import org.springframework.util.ClassUtils;

import java.util.Map;

/**
 * scan all the bean annotated by {@link GrpcController}
 *
 * @see GrpcController
 */
public class GrpcControllerMethodProcessor implements SmartInitializingSingleton, ApplicationContextAware, SmartLifecycle {

    private ApplicationContext applicationContext;

    private GrpcServer grpcServer;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(GrpcController.class);

        // registry provider side invocation metadata
        for (Object object : beansWithAnnotation.values()) {
//            InvocationMetadataRegistry.registryInstance(AopUtils.getTargetClass(object), object); // 实现类内存在多参数方法会报错，此处不注册实现类
            for (Class<?> allInterface : ClassUtils.getAllInterfaces(object)) {
                InvocationMetadataRegistry.registerInstanceMetadata(allInterface, object);
            }
        }

        // init the server
        if (!beansWithAnnotation.isEmpty()) {
            this.grpcServer = applicationContext.getBean(GrpcServer.class);
        }
    }

    @Override
    public void start() {
        if (this.grpcServer != null) {
            this.grpcServer.start();
        }
    }

    @Override
    public void stop() {
        if (this.grpcServer != null) {
            this.grpcServer.stop();
        }
    }

    @Override
    public boolean isRunning() {
        if (this.grpcServer != null) {
            return !this.grpcServer.isStopped();
        }
        return false;
    }
}
