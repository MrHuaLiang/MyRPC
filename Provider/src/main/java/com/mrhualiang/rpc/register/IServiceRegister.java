package com.mrhualiang.rpc.register;

public interface IServiceRegister {

    void register(String serviceName, String serviceIp, String servicePort, String serviceWeight);
}
