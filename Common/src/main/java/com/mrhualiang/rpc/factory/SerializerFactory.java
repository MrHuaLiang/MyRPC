package com.mrhualiang.rpc.factory;

import com.mrhualiang.rpc.serialize.Serializer;

public abstract class SerializerFactory {

    public abstract  Serializer getSerializer();
}
