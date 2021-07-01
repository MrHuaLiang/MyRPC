package com.mrhualiang.provider.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ZkConfig {
    @Value("${Zk.ip}")
    public String ZK_IP;
    @Value("${Zk.port}")
    public String ZK_PORT;
    @Value("${Zk.namespace}")
    public String REGISTER_NAMESPACE;
    @Value("${Zk.timeout}")
    public String SESSION_TIMEOUT;
    @Value("${Zk.defaultmsg}")
    public String DEFAULT_MSG;
}
