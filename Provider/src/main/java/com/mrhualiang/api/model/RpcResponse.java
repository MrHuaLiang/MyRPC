package com.mrhualiang.api.model;

import lombok.Data;

@Data
public class RpcResponse<T> {
    //状态码，200成功，400失败
    private String code;
    private String msg;
    private T result;
}
