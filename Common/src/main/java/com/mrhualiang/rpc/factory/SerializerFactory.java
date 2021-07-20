package com.mrhualiang.rpc.factory;

import com.mrhualiang.rpc.serialize.Serializer;
import com.mrhualiang.rpc.serialize.kryo.KryoSerializer;
import com.mrhualiang.rpc.serialize.protostuff.ProtostuffSerializer;

public abstract class SerializerFactory {

    public abstract  Serializer getSerializer();
}
