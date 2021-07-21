package com.mrhualiang.rpc.server;
/**
 * @author zhuhualiang
 * 服务注册,socket监听
 */

import com.mrhualiang.rpc.annotation.RpcService;
import com.mrhualiang.rpc.config.MyConfig;
import com.mrhualiang.rpc.config.ZkConfig;
import com.mrhualiang.rpc.model.ServiceInfo;
import com.mrhualiang.rpc.register.IServiceRegister;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
public class ZkRpcServer implements ApplicationContextAware, InitializingBean {

    @Resource
    private ZkConfig zkConfig;

    @Value("${server.ip}")
    private String ip;

    @Value("${server.port}")
    private String port;

    private static final ExecutorService executor = Executors.newFixedThreadPool(10);

    @Autowired
    private IServiceRegister registerCenter;

    /**
     * @key 对应的接口类名
     * @value 具体的实例
     */
    private Map<String, Object> beanMappings = new HashMap<>();


    /**
     * 开启监听
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        //nettyRpc();
        socketRpc();
    }

    private void socketRpc() {
        ServerSocket serverSocket = null;
        try {
            //创建socket
            serverSocket = new ServerSocket(Integer.parseInt(port));
            while (true) {
                //监听端口，是个阻塞的方法
                log.info("开始监听{}端口", port);
                Socket socket = serverSocket.accept();
                //处理rpc请求，这里使用线程池来处理
                executor.submit(new RpcServerHandler(beanMappings, socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    log.warn("socket关闭异常,原因是{}", e.getMessage());
                }
            }
        }
    }

    /**
     * 获取添加了RegisterService的注解的服务，将其注册到zookeeper
     *
     * @param context
     */
    @SneakyThrows
    @Override
    public void setApplicationContext(ApplicationContext context) {
        //从spring上下文中获取添加了RegisterService的注解的bean
        String[] beanNames = context.getBeanNamesForAnnotation(RpcService.class);
        log.info("获取需要注册的服务,数量为{}", beanNames.length);
        for (String beanName : beanNames) {
            Object bean = context.getBean(beanName);
            RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
            //获取注解属性
            Class interfaceClass = annotation.interfaceClass();
            String serviceName = annotation.serviceName();
            String servicePort = annotation.port();
            String serviceWeight = annotation.weight();
            ServiceInfo serviceInfo = new ServiceInfo();
            serviceInfo.setName(serviceName);
            serviceInfo.setIp(InetAddress.getLocalHost().getHostAddress());
            serviceInfo.setPort(Integer.parseInt(servicePort));
            serviceInfo.setWeight(Integer.parseInt(serviceWeight));
            //将接口的类名和对应的实例bean的映射关系保存起来
            log.info("根据接口名从容器中获得Bean(实现类对象)并放入本地Map中");
            beanMappings.put(interfaceClass.getName(), bean);
            //注册实例到zk
            registerCenter.register(serviceInfo);
        }
    }
}
