package com.mrhualiang.rpc.impl;

import com.mrhualiang.rpc.service.UserService;
import com.mrhualiang.rpc.annotation.RpcService;
import com.mrhualiang.rpc.domain.User;
import lombok.extern.slf4j.Slf4j;

@RpcService(interfaceClass = UserService.class, serviceName = "UserService", port = "8001", weight = "80")
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public void saveUser(User user) {
        log.info("调用了saveUser方法");
    }

    @Override
    public User getUserById(Integer id) {
        log.info("调用了getUserById方法");
        User user = new User();
        user.setId(1);
        user.setName("Caroline");
        user.setAge(26);
        return user;
    }
}
