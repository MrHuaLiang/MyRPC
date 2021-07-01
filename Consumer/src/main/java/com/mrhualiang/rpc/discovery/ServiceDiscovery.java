package com.mrhualiang.rpc.discovery;

public interface ServiceDiscovery {

    /**
     * 根据服务名称获取服务的真实调用地址
     *
     * @param serviceName
     * @return
     */
    String discover(String serviceName);
}
