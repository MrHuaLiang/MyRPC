package com.mrhualiang.rpc.factory;

import com.mrhualiang.rpc.serialize.Serializer;

public abstract class AbstractSerializerFactory {

    public abstract Serializer getSerializer();
}
