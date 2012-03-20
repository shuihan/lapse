package com.lapse.remoting.core;

import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

public interface RemotingContext {

    public void registerConnection(String group, Connection connection);


    public void unRegisterConnection(String group, Connection connection);


    public void unRegisterAllConnection(String group);
    
    
    public ThreadPoolExecutor getTransportThreadPool();
 
    
    public Connection getConnectionBySession(Session session);
    
    
    public List<Connection> getConnectionByGroup(String group);
}
