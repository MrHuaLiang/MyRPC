package com.mrhualiang.provider.register;

import org.springframework.stereotype.Component;

public interface IServiceRegister {

    void register(String serviceName,String serviceIp,int port);
}
