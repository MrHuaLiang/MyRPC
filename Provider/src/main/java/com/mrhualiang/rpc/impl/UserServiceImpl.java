package com.mrhualiang.rpc.impl;

import com.mrhualiang.rpc.service.UserService;
import com.mrhualiang.rpc.annotation.RpcService;
import com.mrhualiang.rpc.domain.User;

@RpcService(interfaceClass = UserService.class,serviceName = "UserService")
public class UserServiceImpl implements UserService {

    @Override
    public void saveUser(User user) {
        System.out.println("保存User对象:" + user.getName() + "," + user.getAge());
    }

    @Override
    public User getUserById(Integer id) {
        User user = new User();
        user.setId(1);
        user.setName("Caroline");
        user.setAge(26);
        return user;
    }
}
