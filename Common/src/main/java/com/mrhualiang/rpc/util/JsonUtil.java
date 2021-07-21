package com.mrhualiang.rpc.util;

import com.alibaba.fastjson.JSON;

public class JsonUtil {

    public static String JSON2String(Object object){
        return JSON.toJSONString(object);
    }

    public static <T> T String2JSON(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }
}
