package com.hyf.cloudnative.remoting.mesh.config;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.utils.ApplicationUtils;
import com.hyf.cloudnative.remoting.mesh.utils.ServiceHostUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(RemotingProperties.class)
@Import(GrpcServerConfiguration.class)
public class CloudNativeRemotingAutoConfiguration {

    @Bean
    public ServiceHostUtils serviceHostUtil(RemotingProperties properties) {
        return new ServiceHostUtils(properties);
    }

    @Bean
    public ApplicationUtils applicationUtils() {
        return new ApplicationUtils();
    }
}
