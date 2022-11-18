package com.hyf.cloudnative.remoting.mesh.config;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.GrpcControllerMethodProcessor;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.server.GrpcServer;
import io.grpc.BindableService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

@Configuration
public class GrpcServerConfiguration {

    @Resource
    private RemotingProperties properties;

    @Bean // not use bean lifecycle method automatically
    @ConditionalOnMissingBean
    public GrpcServer grpcServer(ObjectProvider<List<BindableService>> bindableServicesProvider) {
        GrpcServer grpcServer = new GrpcServer(properties.getGrpc().getServer());
        bindableServicesProvider.ifAvailable(bindableServices -> grpcServer.getBindableServices().addAll(bindableServices));
        return grpcServer;
    }

    @Bean
    @ConditionalOnMissingBean
    public GrpcControllerMethodProcessor grpcControllerMethodProcessor() {
        return new GrpcControllerMethodProcessor();
    }
}
