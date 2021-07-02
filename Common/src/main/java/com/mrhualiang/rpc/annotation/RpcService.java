package com.mrhualiang.rpc.annotation;

import org.springframework.stereotype.Component;
import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {

    Class interfaceClass();

    String serviceName () ;

    String port();

    String weight();
}
