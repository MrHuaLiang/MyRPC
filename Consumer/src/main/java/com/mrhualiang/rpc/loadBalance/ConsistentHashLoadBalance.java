package com.mrhualiang.rpc.loadBalance;

import com.mrhualiang.rpc.model.ServiceInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("consistentHashLoadBalance")
public class ConsistentHashLoadBalance implements LoadBalance {

    @Override
    public ServiceInfo doSelect(List<ServiceInfo> serviceInfoList) {
        return null;
    }

}
