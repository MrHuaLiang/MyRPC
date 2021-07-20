package com.mrhualiang.rpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class ProviderMain {
    public static void main(String[] args) {
        ApplicationContext run = SpringApplication.run(ProviderMain.class, args);
    }
}
