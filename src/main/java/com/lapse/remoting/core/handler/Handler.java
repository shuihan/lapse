package com.lapse.remoting.core.handler;

import java.io.IOException;
import java.nio.channels.SelectionKey;


/**
 * socket事件处理器
 * 
 * @author shuihan
 * @date 2011-12-27
 */
public interface Handler {

    
    public enum SocketEvent{
        Accept,
        Read,
        Write,
        Connect;
    }
    
    public void handleReadEvent(SelectionKey key);


    public void handleWriteEvent(SelectionKey key);


    public void handleAcceptEvent(SelectionKey key) throws IOException;


    public void handleConnecttedEvent(SelectionKey key) throws IOException;
}
