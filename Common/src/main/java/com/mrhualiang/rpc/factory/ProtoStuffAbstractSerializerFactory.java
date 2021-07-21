package com.mrhualiang.rpc.factory;

import com.mrhualiang.rpc.serialize.Serializer;
import com.mrhualiang.rpc.serialize.protostuff.ProtostuffSerializer;

public class ProtoStuffAbstractSerializerFactory extends AbstractSerializerFactory {
    @Override
    public Serializer getSerializer() {
        return new ProtostuffSerializer();
    }
}
