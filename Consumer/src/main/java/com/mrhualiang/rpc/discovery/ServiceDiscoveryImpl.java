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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class ServiceDiscoveryImpl implements ServiceDiscovery {

    private Map<String, List<String>> serviceMap = new HashMap<>();

    private List<String> serviceAddresses;

    private CuratorFramework curatorFramework;

    @Autowired
    @Qualifier("randomLoadBalance")
    LoadBalance loadBalance;

    @Autowired
    private ZkConfig zkConfig;


    {   // 通过curator连接zk
        curatorFramework = CuratorFrameworkFactory.builder()
                .connectString("121.199.175.227:2181")
                .sessionTimeoutMs(10000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 10)).build();
        //启动
        curatorFramework.start();
    }

    @Override
    public String discover(String serviceName) {
        //根据serviceName获取对应的path
        String nodePath = zkConfig.REGISTER_NAMESPACE + "/" + serviceName;
        log.info("尝试从ZooKeeper中发现服务,路径为{}",nodePath);
        try {
            serviceAddresses = curatorFramework.getChildren().forPath(nodePath);
            log.info("获取服务信息成功,尝试加入本地缓存中");
            addServiceAddress(serviceAddresses, serviceName);
            //动态发现服务节点变化，需要注册监听
            registerWatcher(nodePath, serviceName);
        } catch (Exception e) {
            log.warn("服务发现异常,原因是{}",e.getMessage());
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
                serviceAddresses = curatorFramework.getChildren().forPath(path);
                addServiceAddress(serviceAddresses, serviceName);
                log.info("监听到节点:" + path + "变化为:" + serviceAddresses + "....");
            }
        });
        try {
            pathChildrenCache.start();
        } catch (Exception e) {
            log.info("监听节点变化异常,原因是{}",e.getMessage());
        }
    }

    private void addServiceAddress(List<String> serviceAddresses, String serviceName) {
        if (!CollectionUtils.isEmpty(serviceAddresses)) {
            serviceMap.put(serviceName, serviceAddresses);
        } else {
            log.error("找不到服务{}", serviceName);
        }
    }

}
