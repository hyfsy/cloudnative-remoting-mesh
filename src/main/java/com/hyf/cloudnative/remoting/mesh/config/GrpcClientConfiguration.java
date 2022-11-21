package com.hyf.cloudnative.remoting.mesh.config;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClient;
import com.hyf.cloudnative.remoting.mesh.proxy.http.interceptor.ArgumentAnnotationRequestInterceptor;
import com.hyf.cloudnative.remoting.mesh.proxy.http.interceptor.RequestMappingAnnotationParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

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
    @ConditionalOnMissingBean
    public RequestMappingAnnotationParser requestMappingAnnotationParser() {
        return new RequestMappingAnnotationParser();
    }

    @Bean
    @ConditionalOnMissingBean
    public ArgumentAnnotationRequestInterceptor argumentAnnotationRequestInterceptor() {
        return new ArgumentAnnotationRequestInterceptor();
    }
}
