package com.hyf.cloudnative.remoting.mesh;

import com.hyf.cloudnative.remoting.mesh.config.GrpcClientConfiguration;
import com.hyf.cloudnative.remoting.mesh.proxy.K8SClientFactoryBean;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.*;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

public class K8SClientRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;
    private Environment environment;

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        registerK8SClients(importingClassMetadata, registry);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private void registerK8SClients(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(K8SClient.class));
        Set<String> basePackages = getBasePackages(metadata);
        for (String basePackage : basePackages) {
            candidateComponents.addAll(scanner.findCandidateComponents(basePackage));
        }

        // registry client configuration
        if (!CollectionUtils.isEmpty(candidateComponents)) {
            AbstractBeanDefinition beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(GrpcClientConfiguration.class).getBeanDefinition();
            BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, GrpcClientConfiguration.class.getName());
            BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
        }

        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                // verify annotated class is an interface
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                Assert.isTrue(annotationMetadata.isInterface(), "@K8SClient can only be specified on an interface");

                Map<String, Object> attributes = annotationMetadata
                        .getAnnotationAttributes(K8SClient.class.getCanonicalName());

                registerK8SClient(registry, annotationMetadata, attributes);
            }
        }

    }

    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableK8SClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class<?>[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }

        if (basePackages.isEmpty()) {
            basePackages.add(ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    private void registerK8SClient(BeanDefinitionRegistry registry, AnnotationMetadata annotationMetadata,
                                   Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        Class clazz = ClassUtils.resolveClassName(className, null);
        ConfigurableBeanFactory beanFactory = registry instanceof ConfigurableBeanFactory
                ? (ConfigurableBeanFactory) registry : null;
        K8SClientFactoryBean factoryBean = new K8SClientFactoryBean();
        factoryBean.setBeanFactory(beanFactory);
        factoryBean.setServiceName(getServiceHost(beanFactory, attributes));
        factoryBean.setType(clazz);
        factoryBean.setPort(getServicePort(beanFactory, attributes));
        factoryBean.setTlsEnable((Boolean) attributes.get("tlsEnable"));
        factoryBean.setNamespace((String) attributes.get("namespace"));
        factoryBean.setClusterDomain((String) attributes.get("clusterDomain"));
        factoryBean.setRequestWay((RequestWay) attributes.get("requestWay"));
        factoryBean.setFallback((Class<?>) attributes.get("fallback"));
        factoryBean.setFallbackFactory((Class<?>) attributes.get("fallbackFactory"));

        BeanDefinitionBuilder definition = BeanDefinitionBuilder.genericBeanDefinition(clazz, factoryBean::getObject);
        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
        definition.setLazyInit(true);
        definition.setPrimary(true); // 防止与fallback冲突

        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();

        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className);
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }

    private String getServiceHost(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        if (StringUtils.hasText((String) attributes.get("value"))) {
            return resolve(beanFactory, (String) attributes.get("value"));
        } else {
            return resolve(beanFactory, (String) attributes.get("name"));
        }
    }

    private Integer getServicePort(ConfigurableBeanFactory beanFactory, Map<String, Object> attributes) {
        String port = ((String) attributes.get("port"));
        if (!StringUtils.hasText(port)) {
            return -1;
        }
        try {
            return Integer.parseInt(resolve(beanFactory, port.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client port is invalid: " + attributes.get("port"));
        }
    }

    private String resolve(ConfigurableBeanFactory beanFactory, String value) {
        if (StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }
}
