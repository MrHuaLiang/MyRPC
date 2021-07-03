package com.mrhualiang.rpc.server;

import com.mrhualiang.rpc.factory.SerializerFactory;
import com.mrhualiang.rpc.model.RpcRequest;
import com.mrhualiang.rpc.model.RpcResponse;
import com.mrhualiang.rpc.serialize.Serializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

@Slf4j
public class RpcServerHandler implements Runnable {

    /**
     * 序列化协议,默认kyro
     */
    @Value("${Serialization.protocol}")
    private String protocol;

    private Socket socket;

    private Map<String, Object> serviceMap;

    public RpcServerHandler(Map<String, Object> serviceMap, Socket socket) {
        this.socket = socket;
        this.serviceMap = serviceMap;
    }

    @Override
    public void run() {
        log.info("线程池开始处理RPC请求");
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            ois = new ObjectInputStream(this.socket.getInputStream());
            byte[] bytes = (byte [])ois.readObject();
            Serializer serializer = SerializerFactory.getSerializer(protocol);
            RpcRequest rpcRequest = serializer.deserialize(bytes,RpcRequest.class);
            RpcResponse<Object> result = this.invoke(rpcRequest);
            byte[] bytes2 = serializer.serialize(result);
            oos = new ObjectOutputStream(this.socket.getOutputStream());
            oos.writeObject(bytes2);
            oos.flush();
        } catch (Exception var13) {
            log.warn("处理请求发生异常,原因是{}",var13.getMessage());
        } finally {
            if (oos != null) {
                try {
                    ois.close();
                    oos.close();
                } catch (IOException var12) {
                    log.warn("IO流关闭发生异常,原因是{}",var12.getMessage());
                }
            }

        }

    }

    private RpcResponse<Object> invoke(RpcRequest rpcRequest) {
        String className = rpcRequest.getClassName();
        Object result = null;
        RpcResponse<Object> response = new RpcResponse<>();
        try {
            Class<?> clazz = Class.forName(className);
            Object[] parameters = rpcRequest.getArgs();
            log.info("从本地Map中获取服务Bean对象,开始调用相应的方法");
            Object serviceInstance = this.serviceMap.get(clazz.getName());
            if (parameters == null) {
                Method method = clazz.getMethod(rpcRequest.getMethodName());
                result = method.invoke(serviceInstance);
            } else {
                Class[] types = new Class[parameters.length];
                for(int i = 0; i < parameters.length; ++i) {
                    types[i] = parameters[i].getClass();
                }

                Method method = clazz.getMethod(rpcRequest.getMethodName(), types);
                result = method.invoke(serviceInstance, parameters);
            }
            response.setCode("200");
            response.setMsg("请求成功");
            response.setResult(result);
            response.setId(rpcRequest.getId());
        } catch (Exception e) {
            response.setCode("400");
            response.setMsg("请求异常");
            response.setId(rpcRequest.getId());
            log.info("请求异常,原因是{}",e.getMessage());
        }
        log.info("方法调用结束,返回结果");
        return response;
    }
}