package com.mrhualiang.rpc.service;

import com.mrhualiang.rpc.domain.User;

public interface UserService {
    public void saveUser(User user);

    public User getUserById(Integer id);
}
