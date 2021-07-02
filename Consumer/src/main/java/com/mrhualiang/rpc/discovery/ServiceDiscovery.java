package com.mrhualiang.rpc.discovery;

import com.mrhualiang.rpc.model.ServiceInfo;

public interface ServiceDiscovery {

    /**
     * 根据服务名称获取服务的真实调用地址
     *
     * @param serviceName
     * @return
     */
    ServiceInfo discover(String serviceName);
}
