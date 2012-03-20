package com.lapse.remoting.core.handler.impl;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.apache.log4j.Logger;

import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.core.RemotingContext;
import com.lapse.remoting.core.Session;
import com.lapse.remoting.core.handler.Handler;
import com.lapse.remoting.core.handler.RemotingController;
import com.lapse.remoting.core.impl.LapseSession;
import com.lapse.remoting.core.impl.LapseTcpConnection;
import com.lapse.remoting.util.Constant;


public class SocketEventHandle implements Handler {

    private RemotingContext remotingContext;

    private RemotingController remotingController;

    private Logger logger = Logger.getLogger(SocketEventHandle.class);


    public SocketEventHandle(RemotingContext remotingContext, RemotingController remotingController) {
        this.remotingContext = remotingContext;
        this.remotingController = remotingController;
    }


    @Override
    public void handleReadEvent(SelectionKey key) {
        Session session = (Session) key.attachment();
        if (session == null) {
            logger.warn("Î´×¢²áµÄsession");
            return;
        }
        if (session.isClose()) {
            this.remotingContext.unRegisterConnection(Constant.DEFAULT_GROUP,
                LapseTcpConnection.buildConnection(session, remotingController));
        }
        try {
            SelectableChannel channel = key.channel();
            if (channel.isOpen()) {
                session.onRead(key.selector());
                this.interestOps(session,key,key.interestOps()  | SelectionKey.OP_WRITE);
            }
        }
        catch (Exception e) {
            session.close();
            e.printStackTrace();
        }
    }


    @Override
    public void handleWriteEvent(SelectionKey key) {
        Session session = (Session) key.attachment();
        if (session == null) {
            logger.warn("Î´×¢²áµÄsession");
            return;
        }
        if (session.isClose()) {
            this.remotingContext.unRegisterConnection(Constant.DEFAULT_GROUP,
                LapseTcpConnection.buildConnection(session, remotingController));
        }
        try {
            SelectableChannel channel = key.channel();
            if (channel.isOpen()) {
                session.onWrite(key.selector());
                this.interestOps(session, key, key.interestOps() | SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
        }
        catch (Exception e) {
            session.close();
            e.printStackTrace();
        }

    }


    @Override
    public void handleAcceptEvent(SelectionKey key) throws IOException {
        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = channel.accept();
        socketChannel.configureBlocking(false);

        Session session = LapseSession.buildSession(socketChannel, this.remotingController);
        this.remotingContext.registerConnection(Constant.DEFAULT_GROUP,
            LapseTcpConnection.buildConnection(session, remotingController));
        this.remotingController.registerChannel(socketChannel, SelectionKey.OP_READ, session);
    }


    @Override
    public void handleConnecttedEvent(SelectionKey key) throws IOException {
        Session session = LapseSession.buildSession(key.channel(), this.remotingController);
        this.remotingContext.registerConnection(Constant.DEFAULT_GROUP,
            LapseTcpConnection.buildConnection(session, remotingController));
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.isConnectionPending()) {
            channel.finishConnect();
        }
        Object object = this.remotingController.getAttribute(Constant.CONNECT_LOCK);
        synchronized (object) {
            object.notifyAll();
        }
        // TODO
        System.out.println("client connected " + session);

        this.remotingController.registerChannel(channel, SelectionKey.OP_WRITE, session);
    }


    public SelectionKey interestOps(Session session, SelectionKey key, int ops) {
        LapseActor actor = ((LapseActor) session.getAttachObject(Constant.ACTOR_ATTRIBUTE));
        SelectionKey resKey = key.interestOps(ops);
        if (actor != null) {
            actor.wakeUp();
        }

        return resKey;
    }

}
