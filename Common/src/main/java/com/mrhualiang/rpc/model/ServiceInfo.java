package com.mrhualiang.rpc.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ServiceInfo {

    private String name;

    private String ip;

    private Integer port;

    private Integer weight;

}
