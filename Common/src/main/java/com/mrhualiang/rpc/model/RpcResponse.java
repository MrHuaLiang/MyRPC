package com.mrhualiang.rpc.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RpcResponse <T> implements Serializable {
    //状态码，200成功，400失败
    private String code;
    private String msg;
    private T result;
}
