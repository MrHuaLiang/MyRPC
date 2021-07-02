package com.mrhualiang.rpc.loadBalance;

import com.mrhualiang.rpc.model.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service("randomLoadBalance")
public class RandomLoadBalance implements LoadBalance {

    @Override
    public ServiceInfo doSelect(List<ServiceInfo> serviceInfoList) {
        if (serviceInfoList == null || serviceInfoList.size() == 0) {
            log.error("服务不可用");
            return null;
        } else if (serviceInfoList.size() == 1) {
            return serviceInfoList.get(0);
        } else {
            Random random = new Random();
            return serviceInfoList.get(random.nextInt(serviceInfoList.size()));
        }
    }
}
