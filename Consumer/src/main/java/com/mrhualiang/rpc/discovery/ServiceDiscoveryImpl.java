package com.mrhualiang.rpc.discovery;

import com.mrhualiang.rpc.config.ZkConfig;
import com.mrhualiang.rpc.loadBalance.LoadBalance;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ServiceDiscoveryImpl implements ServiceDiscovery, InitializingBean {

    private Map<String, List<String>> serviceMap = new HashMap<>();

    private List<String> serviceAddresses;

    private CuratorFramework curatorFramework;

    @Autowired
    @Qualifier("randomLoadBalance")
    LoadBalance loadBalance;

    @Autowired
    private ZkConfig zkConfig;


    @Override
    public String discover(String serviceName) {
        //根据serviceName获取对应的path
        String nodePath = zkConfig.REGISTER_NAMESPACE + "/" + serviceName;

        try {
            log.info("尝试从本地缓存中获取服务信息");
            if(serviceMap.get(serviceName) == null){
                log.info("本地缓存中获取服务信息失败");
                log.info("尝试从ZooKeeper中获取服务信息,路径为{}", nodePath);
                serviceAddresses = curatorFramework.getChildren().forPath(nodePath);
                log.info("获取服务信息成功,加入本地缓存");
                addServiceAddress(serviceAddresses, serviceName);
                log.info(serviceAddresses + "");
            }else{
                log.info("成功从本地缓存中获取服务信息");
            }
            //动态发现服务节点变化，需要注册监听
            registerWatcher(nodePath, serviceName);
        } catch (Exception e) {
            log.warn("服务发现异常,原因是{}", e.getMessage());
        }
        return loadBalance.doSelect(serviceMap.get(serviceName));
    }


    /**
     * 监听节点变化，更新serviceAddresses
     *
     * @param path
     */
    private void registerWatcher(final String path, final String serviceName) {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework, path, true);

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                if(pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                    log.info("服务节点增加,更新本地缓存");
                    serviceAddresses = curatorFramework.getChildren().forPath(path);
                    addServiceAddress(serviceAddresses, serviceName);
                }else if(pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED){
                    log.info("服务节点更新,更新本地缓存");
                    serviceAddresses = curatorFramework.getChildren().forPath(path);
                    addServiceAddress(serviceAddresses, serviceName);
                }else{
                    log.info("其他事件,不更新本地缓存");
                }
            }
        });
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            log.info("监听节点变化异常,原因是{}", e.getMessage());
        }
    }

    private void addServiceAddress(List<String> serviceAddresses, String serviceName) {
        if (!CollectionUtils.isEmpty(serviceAddresses)) {
            serviceMap.put(serviceName, serviceAddresses);
        } else {
            log.error("找不到服务{}", serviceName);
            log.info(serviceMap.get(serviceName).get(0));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("与ZooKeeper建立链接");
        curatorFramework = CuratorFrameworkFactory.builder().
                //定义连接串
                        connectString(zkConfig.ZK_IP + ":" + zkConfig.ZK_PORT).
                // 定义session超时时间
                        sessionTimeoutMs(Integer.parseInt(zkConfig.SESSION_TIMEOUT)).
                //定义重试策略
                        retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        //启动
        curatorFramework.start();
    }
}
