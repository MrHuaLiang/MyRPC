package com.mrhualiang.api;

import com.mrhualiang.api.domain.User;

public interface UserService {
    public void saveUser(User user);

    public User getUserById(Integer id);
}
