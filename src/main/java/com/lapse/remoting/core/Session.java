package com.lapse.remoting.core;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;

import com.lapse.remoting.core.handler.RequestCallBack;
import com.lapse.remoting.message.LapseMessage;


public interface Session {

    public SocketAddress getClientSocketAddress();


    public SelectableChannel getChannel();


    public void attachObject(String key, Object object);


    public Object getAttachObject(String key);


    public Object removeAttachObject(String key);


    public boolean write(LapseMessage message) throws IOException;


    public boolean syncWrite(byte[] data) throws IOException;


    public void onRead(Selector selector) throws IOException;


    public void onWrite(Selector selector) throws IOException;


    public SocketAddress getRemotingSocketAddress();


    public SocketAddress getLocalAddress();


    public RequestCallBack addCallBack(long requestId, RequestCallBack callback);


    public RequestCallBack removeCallBack(long requestId);


    public RequestCallBack getCallBack(long requestId);


    public void close();


    public boolean isClose();
}
