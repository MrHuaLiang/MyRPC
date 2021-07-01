package com.mrhualiang.rpc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class CommonMain {
    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(CommonMain.class, args);
    }
}
