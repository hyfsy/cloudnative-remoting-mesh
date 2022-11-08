package com.hyf.cloudnative.remoting.mesh.config;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.GrpcControllerMethodProcessor;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.server.GrpcServer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
public class GrpcServerConfiguration {

    @Resource
    private RemotingProperties properties;

    @Bean // not use bean lifecycle method automatically
    @ConditionalOnMissingBean
    public GrpcServer grpcServer() {
        return new GrpcServer(properties.getGrpc().getServer());
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcControllerMethodProcessor grpcControllerMethodProcessor() {
        return new GrpcControllerMethodProcessor();
    }
}
