package com.mrhualiang.rpc.proxy;

import com.mrhualiang.rpc.discovery.ServiceDiscovery;
import com.mrhualiang.rpc.factory.SerializerFactory;
import com.mrhualiang.rpc.model.RpcRequest;
import com.mrhualiang.rpc.model.RpcResponse;
import com.mrhualiang.rpc.model.ServiceInfo;
import com.mrhualiang.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    /**
     * 序列化协议,默认kyro
     */
    @Value("${Serialization.protocol}")
    private String protocol;

    private String serviceName;

    private ServiceDiscovery serviceDiscovery;

    private static AtomicInteger id = new AtomicInteger(1);

    public RpcInvocationHandler(String serviceName, ServiceDiscovery serviceDiscovery) {
        this.serviceName = serviceName;
        this.serviceDiscovery = serviceDiscovery;
    }

    /**
     * 增强的InvocationHandler,接口调用方法的时候实际是调用socket进行传输
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //将远程调用需要的接口类、方法名、参数信息封装成RPCRequest
        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setId(id.getAndIncrement());
        rpcRequest.setArgs(args);
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        RpcResponse<Object> res = handleSocket(rpcRequest);
        return res.getResult();
    }

    private RpcResponse<Object> handleSocket(RpcRequest rpcRequest) throws Exception {
        ServiceInfo info = serviceDiscovery.discover(serviceName);
        log.info("请求信息{}", rpcRequest);
        log.info("服务地址{}", info.getIp() + ":" + info.getPort());
        //通过socket发送RPCRequest给服务端并获取结果返回
        Socket socket = new Socket(info.getIp(), info.getPort());
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        Serializer serializer = SerializerFactory.getSerializer(protocol);
        byte[] bytes = serializer.serialize(rpcRequest);
        oos.writeObject(bytes);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        byte[] bytes2 = (byte[]) ois.readObject();
        RpcResponse<Object> result = serializer.deserialize(bytes2, RpcResponse.class);
        return result;
    }
}
