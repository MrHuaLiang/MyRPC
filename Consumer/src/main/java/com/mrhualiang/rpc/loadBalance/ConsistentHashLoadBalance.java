package com.mrhualiang.rpc.loadBalance;

import org.springframework.stereotype.Component;
import java.util.List;

@Component("consistentHashLoadBalance")
public class ConsistentHashLoadBalance implements LoadBalance {

    @Override
    public String doSelect(List<String> serviceAddresses) {
        return "";
    }

}
