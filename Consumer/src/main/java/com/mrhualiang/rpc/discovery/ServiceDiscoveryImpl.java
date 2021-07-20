package com.mrhualiang.rpc.discovery;

import com.mrhualiang.rpc.config.MyConfig;
import com.mrhualiang.rpc.config.ZkConfig;
import com.mrhualiang.rpc.loadBalance.LoadBalance;
import com.mrhualiang.rpc.model.ServiceInfo;
import com.mrhualiang.rpc.util.ConvertUtil;
import lombok.SneakyThrows;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ServiceDiscoveryImpl implements ServiceDiscovery, InitializingBean {
    /**
     * 被删除的节点将在缓存中保留一定时间
     */
    @Value("${delay}")
    private Integer time;

    private Map<String, List<ServiceInfo>> serviceMap = new HashMap<>();

    private List<ServiceInfo> serviceInfo;

    private List<String> serviceName;

    private CuratorFramework curatorFramework;

    @Autowired
    @Qualifier("randomLoadBalance")
    LoadBalance loadBalance;

    @Autowired
    private ZkConfig zkConfig;

    /**
     * 根据服务名获取服务信息
     * @param serviceName
     * @return
     */
    @Override
    public ServiceInfo discover(String serviceName) {
        //根据serviceName获取对应的path
        String nodePath = zkConfig.REGISTER_NAMESPACE + "/" + serviceName;

        try {
            if (serviceMap.get(serviceName) == null) {
                log.info("本地缓存中获取服务信息失败");
                log.info("尝试从ZooKeeper中获取服务信息,路径为{}", nodePath);
                serviceInfo = curatorFramework.getChildren().forPath(nodePath).stream().map(ConvertUtil::string2Info).collect(Collectors.toList());
                serviceInfo.forEach(list -> list.setName(serviceName));
                log.info("获取服务信息成功,加入本地缓存");
                addServiceInfo(serviceInfo, serviceName);
                log.info(serviceInfo + "");
            } else {
                log.info("成功从本地缓存中获取服务信息");
            }
            //动态发现服务节点变化，需要注册监听
            registerWatcher(nodePath, serviceName);
        } catch (Exception e) {
            log.warn("服务发现异常,原因是{}", e.getMessage());
        }
        return loadBalance.doSelect(serviceMap.get(serviceName));
    }

    public List<String> discover() {
        String nodePath = zkConfig.REGISTER_NAMESPACE;
        try {
            log.info("获取所有服务");
            serviceName = curatorFramework.getChildren().forPath(nodePath);
            serviceName.forEach(System.out::println);
        } catch (Exception e) {
            log.warn("获取服务异常,原因是{}", e.getMessage());
        }
        return serviceName;
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
                if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED) {
                    log.info("服务节点增加,更新本地缓存");
                    serviceInfo = curatorFramework.getChildren().forPath(path).stream().map(ConvertUtil::string2Info).collect(Collectors.toList());
                    addServiceInfo(serviceInfo, serviceName);
                } else if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED) {
                    log.info("服务节点更新,更新本地缓存");
                    serviceInfo = curatorFramework.getChildren().forPath(path).stream().map(ConvertUtil::string2Info).collect(Collectors.toList());
                    addServiceInfo(serviceInfo, serviceName);
                } else if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                    ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(1);
                    mScheduledExecutorService.schedule(new Runnable() {
                        @SneakyThrows
                        @Override
                        public void run() {
                            serviceInfo = curatorFramework.getChildren().forPath(path).stream().map(ConvertUtil::string2Info).collect(Collectors.toList());
                            addServiceInfo(serviceInfo, serviceName);
                        }
                    }, time, TimeUnit.MINUTES);
                    log.info("服务节点被删除,{}分钟后更新本地缓存", time);
                }
            }
        });
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        } catch (Exception e) {
            log.info("监听节点变化异常,原因是{}", e.getMessage());
        }
    }

    private void addServiceInfo(List<ServiceInfo> serviceInfo, String serviceName) {
        if (!CollectionUtils.isEmpty(serviceInfo)) {
            serviceMap.put(serviceName, serviceInfo);
        } else {
            log.error("没有可用服务{}", serviceName);
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
