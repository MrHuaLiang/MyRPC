package com.mrhualiang.rpc.loadBalance;

import com.mrhualiang.rpc.model.ServiceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service("smoothWeightRoundBalance")
public class SmoothWeightRoundBalance implements LoadBalance {

    private static final Map<String, Integer> map = new HashMap<>();

    @Override
    public ServiceInfo doSelect(List<ServiceInfo> serviceInfoList) {
        if (serviceInfoList == null || serviceInfoList.size() == 0) {
            log.error("服务不可用");
            return null;
        } else if (serviceInfoList.size() == 1) {
            return serviceInfoList.get(0);
        } else {
            serviceInfoList.forEach(serviceInfo -> map.computeIfAbsent(serviceInfo.toString(), key -> serviceInfo.getWeight()));
            ServiceInfo maxWeightService = null;
            int sumWeight = serviceInfoList.stream().mapToInt(ServiceInfo::getWeight).sum();
            for(ServiceInfo serviceInfo : serviceInfoList){
                Integer currentWeight = map.get(serviceInfo.toString());
                if(maxWeightService == null || currentWeight > map.get(maxWeightService.toString())){
                    maxWeightService = serviceInfo;
                }
            }

            assert maxWeightService != null;

            map.put(maxWeightService.toString(),map.get(maxWeightService.toString()) - sumWeight);

            for(ServiceInfo serviceInfo : serviceInfoList){
                Integer currentWeight = map.get(serviceInfo.toString());
                map.put(serviceInfo.toString(), currentWeight + serviceInfo.getWeight());
            }
            return maxWeightService;
        }
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
