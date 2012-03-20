package com.lapse.remoting.core.handler;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

import com.lapse.remoting.core.Session;
import com.lapse.remoting.core.handler.Handler.SocketEvent;
import com.lapse.remoting.message.LapseMessage;


public interface RemotingController {

    public void handleSocketEvent(SelectionKey key, SocketEvent event) throws IOException;


    public void registerChannel(SelectableChannel channel, int ops, Object object) throws IOException;


    public void registerChannel(SelectableChannel channel, int ops) throws IOException;


    public void onMessageArrived(Session session, LapseMessage message);


    public void registerAttribute(String name, Object object);


    public void unRegisterAttribute(String name);


    public Object getAttribute(String name);


    public void setMessageReceiveListener(MessageReceiveListener messageReceiveListener);

}
