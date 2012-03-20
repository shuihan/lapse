package com.lapse.remoting.manager;

import java.util.concurrent.ThreadPoolExecutor;

public interface TransportManager {

    public ThreadPoolExecutor getRemotingThreadPool();
}
