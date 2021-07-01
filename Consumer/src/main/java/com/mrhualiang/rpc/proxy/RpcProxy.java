package com.mrhualiang.rpc.proxy;

import com.mrhualiang.rpc.discovery.ServiceDiscovery;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;

@Component
public class RpcProxy {

    public static <T> T getInstance(Class<T> classInterface, String serviceName, ServiceDiscovery serviceDiscovery) {
        return (T) Proxy.newProxyInstance(classInterface.getClassLoader(), new Class[]{classInterface}, new RpcInvocationHandler(serviceName,serviceDiscovery));
    }
}
