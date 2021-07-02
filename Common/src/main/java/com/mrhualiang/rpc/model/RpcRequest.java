package com.mrhualiang.rpc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcRequest implements Serializable {

    private String className;

    private String methodName;

    private Object[] args;
}
