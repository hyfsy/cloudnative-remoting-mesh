package com.hyf.cloudnative.remoting.mesh.config;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClient;
import com.hyf.cloudnative.remoting.mesh.proxy.http.SpringMVCAnnotationRequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * grpc client configuration
 */
@Configuration
public class GrpcClientConfiguration {

    @Resource
    private RemotingProperties properties;

    @Bean(initMethod = "start", destroyMethod = "stop")
    @ConditionalOnMissingBean
    public GrpcClient grpcClient() {
        return new GrpcClient(properties.getGrpc().getClient());
    }

    @Bean
    public SpringMVCAnnotationRequestInterceptor springMVCAnnotationRequestInterceptor() {
        return new SpringMVCAnnotationRequestInterceptor();
    }
}
