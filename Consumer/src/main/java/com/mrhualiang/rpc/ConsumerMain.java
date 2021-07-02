package com.mrhualiang.rpc;

import com.mrhualiang.rpc.discovery.ServiceDiscovery;
import com.mrhualiang.rpc.discovery.ServiceDiscoveryImpl;
import com.mrhualiang.rpc.domain.School;
import com.mrhualiang.rpc.domain.User;
import com.mrhualiang.rpc.proxy.RpcProxy;
import com.mrhualiang.rpc.service.SchoolService;
import com.mrhualiang.rpc.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@Slf4j
public class ConsumerMain {

    public static void main(String[] args) {
        ApplicationContext ac = SpringApplication.run(ConsumerMain.class, args);
        ServiceDiscovery serviceDiscovery = (ServiceDiscoveryImpl) ac.getBean(ServiceDiscovery.class);
        //由于rpc-server-api里只有实体类和接口类，想要实例化只能通过代理来实现
        UserService userService = RpcProxy.getInstance(UserService.class, "UserService", serviceDiscovery);
        SchoolService schoolService = RpcProxy.getInstance(SchoolService.class, "SchoolService", serviceDiscovery);
        new Thread(()->{
            School school = schoolService.getById(1);
            System.out.println(school.getName());
        }).start();
        new Thread(()->{
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            User user1 = userService.getUserById(1);
            System.out.println(user1.getAge());
        }).start();
    }

}
