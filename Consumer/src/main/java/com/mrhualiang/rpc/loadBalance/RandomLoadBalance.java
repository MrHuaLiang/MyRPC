package com.mrhualiang.rpc.loadBalance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;

@Slf4j
@Component("randomLoadBalance")
public class RandomLoadBalance implements LoadBalance{

    @Override
    public String doSelect(List<String> serviceAddresses) {
        if(serviceAddresses == null || serviceAddresses.size() == 0){
            log.error("服务不可用");
            return null;
        }else if(serviceAddresses.size() == 1){
            return serviceAddresses.get(0);
        }else{
            Random random = new Random();
            return serviceAddresses.get(random.nextInt(serviceAddresses.size()));
        }
    }
}
