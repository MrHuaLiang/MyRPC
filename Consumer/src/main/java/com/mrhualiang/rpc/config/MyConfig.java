package com.mrhualiang.rpc.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class MyConfig {

    @Value("${Zk.ip}")
    public String ZK_IP;

    @Value("${Zk.port}")
    public String ZK_PORT;

    @Value("${Zk.namespace}")
    public String REGISTER_NAMESPACE;

    @Value("${Zk.timeout}")
    public String SESSION_TIMEOUT;

    @Bean
    public ZkConfig zkConfig(){
        ZkConfig zkConfig = new ZkConfig();
        zkConfig.setZK_IP(ZK_IP);
        zkConfig.setZK_PORT(ZK_PORT);
        zkConfig.setREGISTER_NAMESPACE(REGISTER_NAMESPACE);
        zkConfig.setSESSION_TIMEOUT(SESSION_TIMEOUT);
        return zkConfig;
    }

}
