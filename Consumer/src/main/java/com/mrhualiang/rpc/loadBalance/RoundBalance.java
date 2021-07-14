package com.mrhualiang.rpc.loadBalance;

import com.mrhualiang.rpc.model.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("roundBalance")
public class RoundBalance implements LoadBalance {
    private static Integer index = 0;

    @Override
    public ServiceInfo doSelect(List<ServiceInfo> serviceInfoList) {
        if (serviceInfoList == null || serviceInfoList.size() == 0) {
            log.error("服务不可用");
            return null;
        } else if (serviceInfoList.size() == 1) {
            return serviceInfoList.get(0);
        } else {
            if (index == serviceInfoList.size()) index = 0;
            return serviceInfoList.get(index++);
        }
    }
}
