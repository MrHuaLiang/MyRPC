package com.mrhualiang.rpc;

import com.mrhualiang.rpc.discovery.ServiceDiscovery;
import com.mrhualiang.rpc.discovery.ServiceDiscoveryImpl;
import com.mrhualiang.rpc.domain.User;
import com.mrhualiang.rpc.loadBalance.LoadBalance;
import com.mrhualiang.rpc.proxy.RpcProxy;
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
        User user = new User();
        user.setAge(12);
        user.setName("chenpp");
        userService.saveUser(user);
//        User user1 = userService.getUserById(1);
    }

}
