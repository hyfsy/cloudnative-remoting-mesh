package com.hyf.cloudnative.remoting.mesh;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * grpc controller mark, function same as {@link org.springframework.stereotype.Controller},
 * but this annotation must implements client-api interface, otherwise controller will not be
 * registered, and the method in client-api interface must has zero or only one parameter.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface GrpcController {

    /**
     * grpc controller spring bean name
     */
    @AliasFor(annotation = Component.class)
    String value() default "";
}
