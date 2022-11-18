package com.hyf.cloudnative.remoting.mesh.config;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.RemotingApiAcceptor;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.ServerHandler;
import com.hyf.cloudnative.remoting.mesh.utils.ApplicationUtils;
import com.hyf.cloudnative.remoting.mesh.utils.ServiceHostUtils;
import io.grpc.BindableService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(RemotingProperties.class)
@Import(GrpcServerConfiguration.class)
public class CloudNativeRemotingAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServiceHostUtils serviceHostUtil(RemotingProperties properties) {
        return new ServiceHostUtils(properties);
    }

    @Bean
    @ConditionalOnMissingBean
    public ApplicationUtils applicationUtils() {
        return new ApplicationUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public BindableService remotingApiAcceptor() {
        return new RemotingApiAcceptor(new ServerHandler());
    }
}
