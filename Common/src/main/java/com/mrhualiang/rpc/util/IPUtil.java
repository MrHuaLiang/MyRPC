package com.mrhualiang.rpc.util;

import lombok.extern.slf4j.Slf4j;
import java.net.InetAddress;

@Slf4j
public class IPUtil {


    /**
     * 获取本机ip
     */
    public static String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.info("获取本机IP失败,返回127.0.0.1");
            return "127.0.0.1";
        }
    }
}
