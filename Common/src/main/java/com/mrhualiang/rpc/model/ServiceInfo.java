package com.mrhualiang.rpc.model;

import lombok.Data;

@Data
public class ServiceInfo {

    private String name;

    private String ip;

    private Integer port;

    private Integer weight;

    @Override
    public String toString() {
        return "{" +
                "name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", weight=" + weight +
                '}';
    }
}
