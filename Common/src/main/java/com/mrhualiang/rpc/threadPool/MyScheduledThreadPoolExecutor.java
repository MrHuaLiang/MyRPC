package com.mrhualiang.rpc.threadPool;

import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.NANOSECONDS;

public class MyScheduledThreadPoolExecutor extends ThreadPoolExecutor {

    public MyScheduledThreadPoolExecutor(int corePoolSize, int maximumPoolSizer) {
        super(corePoolSize, maximumPoolSizer, 0, TimeUnit.MILLISECONDS, new DelayQueue<>(), Executors.defaultThreadFactory(), new DiscardPolicy());
    }
}
