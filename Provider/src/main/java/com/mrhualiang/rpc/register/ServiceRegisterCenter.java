package com.mrhualiang.rpc.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.mrhualiang.rpc.config.*;

@Component
@Slf4j
public class ServiceRegisterCenter implements IServiceRegister {

    @Autowired
    private ZkConfig zkConfig;

    private CuratorFramework curatorFramework;


    {   // 通过curator连接zk
        curatorFramework = CuratorFrameworkFactory.builder().
                //定义连接串
                        connectString("121.199.175.227:2181").
                // 定义session超时时间
                        sessionTimeoutMs(10000).
                //定义重试策略
                        retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        //启动
        curatorFramework.start();
    }

    /**
     * 向ZooKeeper中注册服务
     * @param serviceName 服务名称
     * @param serviceIp   IP
     * @param port        端口
     */
    @Override
    public void register(String serviceName, String serviceIp, int port) {
        log.info("ZooKeeper地址为{}", zkConfig.ZK_IP + ":" + zkConfig.ZK_PORT);
        log.info("向ZooKeeper注册服务");
        //注册相应的服务 注意 zk注册的节点名称需要以/开头
        String servicePath = zkConfig.REGISTER_NAMESPACE + "/" + serviceName;
        try {
            //判断 /${registerPath}/${serviceName}节点是否存在，不存在则创建对应的持久节点
            if (curatorFramework.checkExists().forPath(servicePath) == null) {
                curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(servicePath, "0".getBytes());
            }
            //设置节点的value为对应的服务地址信息(临时节点)
            String serviceAddress = servicePath + "/" + serviceIp + ":" + port;
            String zkNode = curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(serviceAddress, "0".getBytes());
            log.info(serviceName + "服务,地址:" + serviceAddress + " 注册成功:" + zkNode);
        } catch (Exception e) {
            log.warn("注册服务发生异常,原因是{}",e.getMessage());
        }
    }
}
