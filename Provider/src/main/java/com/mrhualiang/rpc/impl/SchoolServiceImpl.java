package com.mrhualiang.rpc.impl;

import com.mrhualiang.rpc.annotation.RpcService;
import com.mrhualiang.rpc.domain.School;
import com.mrhualiang.rpc.service.SchoolService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RpcService(interfaceClass = SchoolService.class, serviceName = "SchoolService", ip = "${server.ip}", port = "${Integer.parseInt(server.ip)}", weight = "20")
public class SchoolServiceImpl implements SchoolService {

    @Override
    public void saveSchool(School school) {
        log.info("调用了saveSchool方法");
    }

    @Override
    public School getById(Integer id) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("调用了getById方法");
        School school = new School();
        school.setId(id);
        school.setName("武汉大学");
        school.setAddress("珞珈山");
        return school;
    }
}
