package com.mrhualiang.rpc.annotation;

import com.mrhualiang.rpc.model.ServiceInfo;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    Class interfaceClass() ;

    String serviceName () ;

    String ip () ;

    String port();

    String weight();
}
