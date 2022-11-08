package com.hyf.cloudnative.remoting.mesh;

import com.hyf.cloudnative.remoting.mesh.proxy.FallbackFactory;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * marker as a k8s service invoke client
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface K8SClient {

    /**
     * k8s service name
     */
    @AliasFor("name")
    String value() default "";

    /**
     * k8s service name
     */
    @AliasFor("value")
    String name() default "";

    /**
     * k8s service export port, http default 8080, grpc default 5443
     */
    String port() default "";

    /**
     * whether the service connection is encrypted
     */
    boolean tlsEnable() default false;

    /**
     * k8s service namespace
     */
    String namespace() default "";

    /**
     * k8s cluster domain suffix
     */
    String clusterDomain() default "";

    /**
     * request way
     * <p>
     * current only support http or grpc
     */
    RequestWay requestWay() default RequestWay.GRPC;

    /**
     * Fallback class for the specified Feign client interface. The fallback class must
     * implement the interface annotated by this annotation and be a valid spring bean.
     * @return fallback class for the specified Feign client interface
     */
    Class<?> fallback() default void.class;

    /**
     * Define a fallback factory for the specified Feign client interface. The fallback
     * factory must produce instances of fallback classes that implement the interface
     * annotated by {@link K8SClient}. The fallback factory must be a valid spring bean.
     *
     * @see FallbackFactory for details.
     * @return fallback factory for the specified Feign client interface
     */
    Class<?> fallbackFactory() default void.class;

}
