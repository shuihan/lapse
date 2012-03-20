package com.lapse.remoting.client.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeoutException;

import com.lapse.remoting.client.LapseClient;
import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.core.RemotingContext;
import com.lapse.remoting.core.handler.Handler.SocketEvent;
import com.lapse.remoting.core.handler.MessageReceiveListener;
import com.lapse.remoting.core.impl.LapseRemotingContext;
import com.lapse.remoting.core.impl.SelectorManager;
import com.lapse.remoting.manager.TransportManager;
import com.lapse.remoting.manager.TransportManagerImpl;
import com.lapse.remoting.util.Constant;


public class DefaultLapseClient implements LapseClient {

    private LapseClientConfig lapseClientConfig;

    private SelectorManager selectorManager;

    private RemotingContext remotingContext;

    private TransportManager transportManager;


    public DefaultLapseClient(LapseClientConfig lapseClientConfig, MessageReceiveListener messageReceiveListener) {
        this.lapseClientConfig = lapseClientConfig;
        this.transportManager = new TransportManagerImpl(this.lapseClientConfig);
        this.remotingContext = new LapseRemotingContext(this.transportManager);
        this.selectorManager = new SelectorManager(this.remotingContext, messageReceiveListener);
    }


    public DefaultLapseClient(LapseClientConfig lapseClientConfig) {
        this(lapseClientConfig, null);
    }


    private SocketChannel createAndConfigChannel() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.socket().setReuseAddress(this.lapseClientConfig.isReuseAddress());
        socketChannel.socket().setKeepAlive(this.lapseClientConfig.isKeepLive());
        socketChannel.socket().setReceiveBufferSize(this.lapseClientConfig.getReceiveBuffSize());
        socketChannel.socket().setSendBufferSize(this.lapseClientConfig.getSendBuffSize());
        socketChannel.socket().setSoLinger(this.lapseClientConfig.isSoLinger(),
            this.lapseClientConfig.getSoLingerTimeOut());
        socketChannel.socket().setSoTimeout(this.lapseClientConfig.getTimeout());
        socketChannel.socket().setTcpNoDelay(this.lapseClientConfig.isTcpDelay());

        socketChannel.configureBlocking(false);

        return socketChannel;
    }


    @Override
    public void connect(InetSocketAddress serverAddress) throws IOException {
        SocketChannel socketChannel = createAndConfigChannel();
        boolean connect = socketChannel.connect(serverAddress);
        if (!connect) {
            socketChannel.register(selectorManager.getActor().getSelector(), SelectionKey.OP_CONNECT);
        }
        else {
            SelectionKey key = this.selectorManager.getKeyForChannel(socketChannel);
            if (key != null) {
                this.selectorManager.getRemotingController().handleSocketEvent(key, SocketEvent.Connect);
            }
            else {
                throw new RuntimeException("注册的selector中找不到对应channel的key");
            }
        }
    }


    @Override
    public void close() throws IOException {
        for (LapseActor actor : this.selectorManager.getActors()) {
            for (SelectionKey key : actor.getSelector().keys()) {
                key.channel().close();
                key.cancel();
            }
        }
    }


    @Override
    public void connect() throws IOException {
        InetSocketAddress serverAddress =
                new InetSocketAddress(this.lapseClientConfig.getIp(), this.lapseClientConfig.getPort());
        this.connect(serverAddress);
    }


    @Override
    public RemotingContext getRemotingContext() {
        return this.remotingContext;
    }


    @Override
    public void connect(String serverIp, int port) throws IOException {
        InetSocketAddress address = new InetSocketAddress(serverIp, port);
        this.connect(address);
    }


    @Override
    public void waitForConnectionReady(long timeout) throws InterruptedException, TimeoutException {
        Object object = this.selectorManager.getRemotingController().getAttribute(Constant.CONNECT_LOCK);
        long start = System.currentTimeMillis();
        synchronized (object) {
            object.wait(timeout);
            long end = System.currentTimeMillis();
            if ((end - start) >= timeout) {
                throw new TimeoutException("等待连接超时！");
            }
        }
    }


    @Override
    public void setMessageReceiveListener(MessageReceiveListener messageReceiveListener) {
        this.selectorManager.setMessageReceiveListener(messageReceiveListener);
    }
}
