package com.mrhualiang.rpc.loadBalance;

import java.util.List;

public interface LoadBalance {
    String doSelect(List<String> serviceAddresses);
}
