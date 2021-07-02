package com.mrhualiang.rpc.proxy;

import com.mrhualiang.rpc.discovery.ServiceDiscovery;
import com.mrhualiang.rpc.model.RpcRequest;
import com.mrhualiang.rpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

@Slf4j
public class RpcInvocationHandler implements InvocationHandler {

    private String serviceName;

    private ServiceDiscovery serviceDiscovery;

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
        rpcRequest.setArgs(args);
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        rpcRequest.setMethodName(method.getName());
        //return handleNetty(rpcRequest);
        return handleSocket(rpcRequest).getResult();
    }

    private RpcResponse<Object> handleSocket(RpcRequest rpcRequest) throws IOException, ClassNotFoundException {
        String address = serviceDiscovery.discover(serviceName);
        //绑定端口启动netty客户端
        String[] add = address.split(":");
        //通过socket发送RPCRequest给服务端并获取结果返回
        Socket socket= new Socket(add[0],Integer.parseInt(add[1]));
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(rpcRequest);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        RpcResponse<Object> result = (RpcResponse<Object>) ois.readObject();
        return result;
    }
}
