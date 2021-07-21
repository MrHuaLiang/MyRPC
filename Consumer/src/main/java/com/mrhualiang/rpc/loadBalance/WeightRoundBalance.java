package com.mrhualiang.rpc.loadBalance;

import com.mrhualiang.rpc.model.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service("weightRoundBalance")
public class WeightRoundBalance implements LoadBalance {

    private static int index;

    @Override
    public ServiceInfo doSelect(List<ServiceInfo> serviceInfoList) {
        if (serviceInfoList == null || serviceInfoList.size() == 0) {
            log.error("服务不可用");
            return null;
        } else if (serviceInfoList.size() == 1) {
            return serviceInfoList.get(0);
        } else {
            int sumWeight = serviceInfoList.stream().mapToInt(ServiceInfo::getWeight).sum();
            int num = (index++) % sumWeight;
            for (ServiceInfo serviceInfo : serviceInfoList) {
                if (serviceInfo.getWeight() > num) {
                    return serviceInfo;
                }
                num -= serviceInfo.getWeight();
            }
            return null;
        }
    }
}
