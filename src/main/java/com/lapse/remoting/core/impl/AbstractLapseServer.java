package com.lapse.remoting.core.impl;

import com.lapse.remoting.core.LapseServer;
import com.lapse.remoting.core.ManagerLifecycle;

public abstract class AbstractLapseServer implements LapseServer,ManagerLifecycle {

    public abstract boolean isStart();
}
