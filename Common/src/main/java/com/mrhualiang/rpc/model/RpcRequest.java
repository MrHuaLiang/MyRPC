package com.mrhualiang.rpc.model;

import lombok.Data;

@Data
public class RpcRequest {

    private String className;

    private String methodName;

    private Object[] args;
}
