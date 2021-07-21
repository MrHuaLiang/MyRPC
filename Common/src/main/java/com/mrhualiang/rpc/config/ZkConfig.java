package com.mrhualiang.rpc.config;

import lombok.Data;

@Data
public class ZkConfig {

    public String ZK_IP;

    public String ZK_PORT;

    public String REGISTER_NAMESPACE;

    public String SESSION_TIMEOUT;

}
