package com.mrhualiang.rpc.loadBalance;

import com.mrhualiang.rpc.model.ServiceInfo;

import java.util.List;

public interface LoadBalance {
    /**
     *
     * @param serviceInfo 服务信息，格式：ip:port,weight
     * @return 选择的服务地址
     */
    ServiceInfo doSelect(List<ServiceInfo> serviceInfo);
}
