package com.mrhualiang.rpc.loadBalance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class WeightRoundBalance implements LoadBalance {

    private static int index;

    @Override
    public String doSelect(List<String> serviceInfoList) {
        if (serviceInfoList == null || serviceInfoList.size() == 0) {
            log.error("服务不可用");
            return null;
        } else if (serviceInfoList.size() == 1) {
            return serviceInfoList.get(0);
        } else {
            int sumWeight = serviceInfoList.stream().mapToInt(info -> Integer.parseInt(info.split(",")[1])).sum();
            int num = (index++) % sumWeight;
            for (String serviceInfo : serviceInfoList) {
                if (Integer.parseInt(serviceInfo.split(",")[1]) > num) {
                    return serviceInfo;
                }
                num -= Integer.parseInt(serviceInfo.split(",")[1]);
            }
            return null;
        }
    }
}
