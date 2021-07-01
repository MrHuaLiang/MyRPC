package com.mrhualiang.rpc.proxy;

import com.mrhualiang.api.model.RpcRequest;
import com.mrhualiang.rpc.discovery.ServiceDiscovery;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;

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
        return handleSocket(rpcRequest);
    }

    private Object handleSocket(RpcRequest rpcRequest) throws IOException, ClassNotFoundException {
        String address = serviceDiscovery.discover(serviceName);
        //绑定端口启动netty客户端
        String[] add = address.split(":");
        //通过socket发送RPCRequest给服务端并获取结果返回
        Socket socket= new Socket(add[0],Integer.parseInt(add[1]));
        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
        oos.writeObject(rpcRequest);
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
        Object result = ois.readObject();
        return result;
    }
}
