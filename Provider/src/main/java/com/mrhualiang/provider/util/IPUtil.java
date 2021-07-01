package com.mrhualiang.provider.util;

import java.net.InetAddress;

public class IPUtil {


    /**
     * 获取本机ip
     */
    public static String getLocalHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            System.out.println("获取本机ip失败");
            return "127.0.0.1";
        }
    }
}
