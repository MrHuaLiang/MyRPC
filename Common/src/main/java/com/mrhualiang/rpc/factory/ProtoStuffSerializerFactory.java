package com.mrhualiang.rpc.factory;

import com.mrhualiang.rpc.serialize.Serializer;
import com.mrhualiang.rpc.serialize.protostuff.ProtostuffSerializer;

public class ProtoStuffSerializerFactory extends SerializerFactory{
    @Override
    public Serializer getSerializer() {
        return new ProtostuffSerializer();
    }
}
