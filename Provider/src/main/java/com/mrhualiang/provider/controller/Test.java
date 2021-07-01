package com.mrhualiang.provider.controller;

import com.mrhualiang.api.UserService;
import com.mrhualiang.api.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @Autowired
    UserService userService;

    @RequestMapping("/test")
    public User test(){
        User user = userService.getUserById(2);
        return user;
    }

}
