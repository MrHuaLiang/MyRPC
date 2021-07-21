package com.mrhualiang.rpc.register;

import com.mrhualiang.rpc.model.ServiceInfo;

public interface ServiceRegister {

    void register(ServiceInfo serviceInfo);

    /**
     * 虽然停止服务后会自动注销服务，但是有时我们希望能主动注销
     * @param serviceInfo
     */
    void unregister(ServiceInfo serviceInfo);
}
