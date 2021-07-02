package com.mrhualiang.rpc;

import com.mrhualiang.rpc.server.ZkRpcServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@Slf4j
public class ProviderMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(ProviderMain.class, args);
    }
}
