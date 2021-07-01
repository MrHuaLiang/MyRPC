package com.mrhualiang.rpc.loadBalance;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("consistentHashLoadBalance")
public class ConsistentHashLoadBalance implements LoadBalance {

    @Override
    public String doSelect(List<String> serviceAddresses) {
        return "";
    }

}
