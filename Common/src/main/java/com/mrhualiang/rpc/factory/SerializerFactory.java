package com.mrhualiang.rpc.factory;

import com.mrhualiang.rpc.serialize.Serializer;
import com.mrhualiang.rpc.serialize.kryo.KryoSerializer;
import com.mrhualiang.rpc.serialize.protostuff.ProtostuffSerializer;

public class SerializerFactory {

    public static Serializer getSerializer(String serializer){
        if("kryo".equalsIgnoreCase(serializer)){
            return new KryoSerializer();
        }else if("protostuff".equalsIgnoreCase(serializer)){
            return new ProtostuffSerializer();
        }else{
            return new KryoSerializer();
        }
    }
}
