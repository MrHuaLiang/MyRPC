package com.mrhualiang.provider.impl;

import com.mrhualiang.api.UserService;
import com.mrhualiang.api.domain.User;
import com.mrhualiang.provider.annotation.RpcService;

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
