package com.lapse.remoting.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.TimeoutException;

import com.lapse.remoting.core.RemotingContext;
import com.lapse.remoting.core.handler.MessageReceiveListener;


public interface LapseClient {

    public void connect(InetSocketAddress serverAddress) throws IOException;


    public void connect() throws IOException;


    public void connect(String serverIp, int port) throws IOException;


    public RemotingContext getRemotingContext();


    public void waitForConnectionReady(long timeout) throws InterruptedException, TimeoutException;

    
    public void setMessageReceiveListener(MessageReceiveListener messageReceiveListener);
    

    public void close() throws IOException;
}
