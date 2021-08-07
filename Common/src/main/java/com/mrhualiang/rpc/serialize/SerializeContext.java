package com.mrhualiang.rpc.serialize;

import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

public class SerializeContext {
    Serializer serializer;

    public SerializeContext(Serializer serializer){
        this.serializer = serializer;
    }
    public byte[] serialize(Object obj) throws Exception {
        return serializer.serialize(obj);
    }

    public <T> T deserialize(byte[] bytes, Class<T> clazz) throws Exception {
        return serializer.deserialize(bytes, clazz);
    }
}
