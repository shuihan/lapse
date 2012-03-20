package com.lapse.remoting.manager;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.lapse.remoting.config.LapseConfig;


public class TransportManagerImpl implements TransportManager {

    private LapseConfig lapseConfig;


    public TransportManagerImpl(LapseConfig lapseConfig) {
        this.lapseConfig = lapseConfig;
    }

    @Override
    public ThreadPoolExecutor getRemotingThreadPool() {
        ThreadPoolExecutor executor =
                new ThreadPoolExecutor(lapseConfig.getCorePoolSize(), lapseConfig.getMaxPoolSize(),
                    lapseConfig.getLiveTime(), TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        return executor;
    }

}
