package com.mrhualiang.rpc.factory;

import com.mrhualiang.rpc.serialize.Serializer;
import com.mrhualiang.rpc.serialize.kryo.KryoSerializer;

public class KryoSerializerFactory extends SerializerFactory{
    @Override
    public Serializer getSerializer() {
        return new KryoSerializer();
    }
}
