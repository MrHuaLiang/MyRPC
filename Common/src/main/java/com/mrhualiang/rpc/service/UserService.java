package com.mrhualiang.rpc.service;

import com.mrhualiang.rpc.domain.User;

public interface UserService {

    void saveUser(User user);

    User getUserById(Integer id);

}
