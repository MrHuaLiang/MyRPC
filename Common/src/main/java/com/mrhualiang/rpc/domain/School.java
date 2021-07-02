package com.mrhualiang.rpc.domain;

import lombok.Data;

import java.io.Serializable;
@Data
public class School implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;

    private String name;

    private String address;
}
