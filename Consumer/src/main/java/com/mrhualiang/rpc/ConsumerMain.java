package com.mrhualiang.rpc;

import com.mrhualiang.rpc.loadBalance.RandomLoadBalance;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ConsumerMain {

    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(ConsumerMain.class, args);
        RandomLoadBalance bean = ac.getBean(RandomLoadBalance.class);
        System.out.println(bean);
    }

}
