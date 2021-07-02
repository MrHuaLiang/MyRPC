package com.mrhualiang.rpc.service;

import com.mrhualiang.rpc.domain.School;

public interface SchoolService {

    void saveSchool(School school);

    School getById(Integer id);

}
