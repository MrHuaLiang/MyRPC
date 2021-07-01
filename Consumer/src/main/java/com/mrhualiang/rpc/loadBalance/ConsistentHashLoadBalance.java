package com.mrhualiang.rpc.loadBalance;

import java.util.List;


public class ConsistentHashLoadBalance implements LoadBalance {

    @Override
    public String doSelect(List<String> serviceAddresses) {
        return "";
    }

}
