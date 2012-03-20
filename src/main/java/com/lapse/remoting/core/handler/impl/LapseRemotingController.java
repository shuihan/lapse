package com.lapse.remoting.core.handler.impl;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ConcurrentHashMap;

import com.lapse.remoting.core.Connection;
import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.core.RemotingContext;
import com.lapse.remoting.core.Session;
import com.lapse.remoting.core.handler.Handler;
import com.lapse.remoting.core.handler.Handler.SocketEvent;
import com.lapse.remoting.core.handler.MessageReceiveListener;
import com.lapse.remoting.core.handler.RemotingController;
import com.lapse.remoting.core.handler.RequestCallBack;
import com.lapse.remoting.core.impl.SelectorManager;
import com.lapse.remoting.message.LapseMessage;
import com.lapse.remoting.util.Command;
import com.lapse.remoting.util.Constant;


public class LapseRemotingController implements RemotingController {

    private RemotingContext remotingContext;

    private SelectorManager selectorManager;

    private MessageReceiveListener messageReceiveListener;

    private Handler handle;

    private final ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<String, Object>();


    public LapseRemotingController(RemotingContext remotingContext, SelectorManager selectorManager,
            MessageReceiveListener messageReceiveListener) {
        this.remotingContext = remotingContext;
        this.selectorManager = selectorManager;
        this.messageReceiveListener = messageReceiveListener;
        this.handle = new SocketEventHandle(this.remotingContext, this);
        this.registerAttribute(Constant.CONNECT_LOCK, new Object());
    }


    protected void onAccept(SelectionKey key) throws IOException {
        this.handle.handleAcceptEvent(key);
    }


    protected void onRead(final SelectionKey key) throws IOException {
        handle.handleReadEvent(key);
    }


    protected void onWrite(final SelectionKey key) throws IOException {
        handle.handleWriteEvent(key);
    }


    protected void onConnect(SelectionKey key) throws IOException {
        this.handle.handleConnecttedEvent(key);
    }


    @Override
    public void registerChannel(SelectableChannel channel, int ops, Object object) throws IOException {
        if (object == null) {
            return;
        }
        Session session = (Session) object;
        if (session.getAttachObject(Constant.ACTOR_ATTRIBUTE) != null) {
            ((LapseActor) session.getAttachObject(Constant.ACTOR_ATTRIBUTE)).registerChannel(channel, ops, object);
        }
        else {
            LapseActor actor = this.selectorManager.getActor();
            session.attachObject(Constant.ACTOR_ATTRIBUTE, actor);
            actor.registerChannel(channel, ops, object);
        }

    }


    @Override
    public void registerChannel(SelectableChannel channel, int ops) throws IOException {
        this.registerChannel(channel, ops, null);
    }


    @Override
    public void onMessageArrived(final Session session, final LapseMessage message) {
        Command command = message.getCommond();
        switch (command) {
        case REQUEST:
            this.receiveMessage(session, message);
            break;
        case RESPONSE:
            this.response(session, message);
            break;
        }
    }


    private void response(final Session session, final LapseMessage message) {
        RequestCallBack callback = session.getCallBack(message.getRequestId());
        if (callback != null) {
            callback.onResponse(message.getRequestId(), message.getData());
        }
    }


    private void receiveMessage(final Session session, final LapseMessage message) {
        if (this.messageReceiveListener != null) {
            final Connection connection = remotingContext.getConnectionBySession(session);
            if (this.messageReceiveListener.getExecutor() != null) {
                this.messageReceiveListener.getExecutor().execute(new Runnable() {

                    @Override
                    public void run() {
                        messageReceiveListener.receivedMessage(connection, message);
                    }
                });
            }
            else {
                this.messageReceiveListener.receivedMessage(connection, message);
            }
        }
    }


    @Override
    public void handleSocketEvent(final SelectionKey key, final SocketEvent event) throws IOException {
        innerHandleEvent(key, event);
    }


    private void innerHandleEvent(SelectionKey key, SocketEvent event) throws IOException {
        switch (event) {
        case Accept:
            this.onAccept(key);
            break;
        case Read:
            this.onRead(key);
            break;
        case Write:
            this.onWrite(key);
            break;
        case Connect:
            this.onConnect(key);
            break;
        }

    }


    public void setMessageReceiveListener(MessageReceiveListener messageReceiveListener) {
        this.messageReceiveListener = messageReceiveListener;
    }


    @Override
    public void registerAttribute(String name, Object object) {
        this.attributes.put(name, object);
    }


    @Override
    public void unRegisterAttribute(String name) {
        this.attributes.remove(name);
    }


    @Override
    public Object getAttribute(String name) {
        return this.attributes.get(name);
    }

}
