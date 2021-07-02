package com.mrhualiang.rpc.util;

import com.mrhualiang.rpc.model.ServiceInfo;

public class ConvertUtil {

    public static ServiceInfo string2Info(String infoString){
        String serviceIp = infoString.split(":")[0];
        String servicePort = infoString.split(":")[1].split(",")[0];
        String serviceWeight = infoString.split(",")[1];
        ServiceInfo serviceInfo = new ServiceInfo();
        serviceInfo.setIp(serviceIp);
        serviceInfo.setPort(Integer.parseInt(servicePort));
        serviceInfo.setWeight(Integer.parseInt(serviceWeight));
        return serviceInfo;
    }
}
