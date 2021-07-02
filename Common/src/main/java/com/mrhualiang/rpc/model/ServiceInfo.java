package com.mrhualiang.rpc.model;

import lombok.Data;

@Data
public class ServiceInfo {

    private String ip;

    private String port;

    private String weight;

}
