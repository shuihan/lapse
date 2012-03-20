package com.lapse.remoting.core.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

import com.lapse.remoting.config.LapseConfig;
import com.lapse.remoting.core.RemotingContext;
import com.lapse.remoting.core.handler.MessageReceiveListener;
import com.lapse.remoting.manager.TransportManager;
import com.lapse.remoting.manager.TransportManagerImpl;


public class LapseTCPServer extends AbstractLapseServer {

    private ServerSocketChannel serverSocketChannel;

    private SelectorManager selectorManager;

    private LapseConfig lapseConfig;

    private RemotingContext remotingContext;

    private boolean isStart = false;

    private TransportManager transportManager;

    private MessageReceiveListener messageReceiveListener;


    public LapseTCPServer(LapseConfig lapseConfig) {
        this.lapseConfig = lapseConfig;
        this.transportManager = new TransportManagerImpl(this.lapseConfig);
        this.remotingContext = new LapseRemotingContext(this.transportManager);
        this.selectorManager = new SelectorManager(this.remotingContext, null);
    }


    public LapseTCPServer(LapseConfig lapseConfig, MessageReceiveListener messageReceiveListener) {
        this.lapseConfig = lapseConfig;
        this.messageReceiveListener = messageReceiveListener;
        this.transportManager = new TransportManagerImpl(this.lapseConfig);
        this.remotingContext = new LapseRemotingContext(this.transportManager);
        this.selectorManager = new SelectorManager(this.remotingContext, this.messageReceiveListener);
    }


    public TransportManager getTransportManager() {
        return transportManager;
    }


    private void init() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().setSoTimeout(lapseConfig.getTimeout());
        serverSocketChannel.socket().bind(new InetSocketAddress(lapseConfig.getPort()));
        serverSocketChannel.configureBlocking(lapseConfig.isBlock());
        serverSocketChannel.socket().setReuseAddress(this.lapseConfig.isReuseAddress());
        serverSocketChannel.socket().setSoTimeout(this.lapseConfig.getTimeout());
        serverSocketChannel.register(selectorManager.getActor(0).getSelector(), SelectionKey.OP_ACCEPT);
    }


    public void start() {
        try {
            this.init();
            isStart = true;
            // TODO
            System.out.println("server startup: ip=" + this.serverSocketChannel.socket().getInetAddress() + ","
                    + "port=" + this.serverSocketChannel.socket().getLocalPort());
        }
        catch (IOException e) {
            isStart = false;
            e.printStackTrace();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("start !");
    }


    public void stop() {
        try {
            serverSocketChannel.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public int startIndex() {
        return 0;
    }


    @Override
    public boolean isStart() {
        return this.isStart;
    }
    
    public MessageReceiveListener getMessageReceiveListener() {
        return messageReceiveListener;
    }


    public void setMessageReceiveListener(MessageReceiveListener messageReceiveListener) {
        this.messageReceiveListener = messageReceiveListener;
        this.selectorManager.setMessageReceiveListener(messageReceiveListener);
    }

}
