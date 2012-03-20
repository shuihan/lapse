package com.lapse.remoting.core.impl;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.lapse.remoting.client.SendResult;
import com.lapse.remoting.core.Connection;
import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.core.Session;
import com.lapse.remoting.core.TimerTask;
import com.lapse.remoting.core.handler.RemotingController;
import com.lapse.remoting.core.handler.RequestCallBack;
import com.lapse.remoting.core.handler.impl.LapseRequestCallBack;
import com.lapse.remoting.message.LapseMessage;
import com.lapse.remoting.util.Command;
import com.lapse.remoting.util.Constant;
import com.lapse.remoting.util.IDGenerator;


/**
 * TCP Connection 的默认实现
 * 
 * @author Administrator
 * 
 */
public class LapseTcpConnection implements Connection {

    private Session session;



    public Session getSession() {
        return session;
    }


    public LapseTcpConnection(Session session, RemotingController remotingController) {
        this.session = session;
    }


    public static LapseTcpConnection buildConnection(Session session, RemotingController remotingController) {
        return new LapseTcpConnection(session, remotingController);
    }


    @Override
    public void sendOneWay(byte[] data) throws IOException {
        long requestId = IDGenerator.nextId();
        LapseMessage msg = new LapseMessage(requestId, data, Command.REQUEST);
        this.session.write(msg);
    }


    public SendResult send(final byte[] data) throws IOException {
        return this.send(data, 3000, TimeUnit.MILLISECONDS);
    }


    public SendResult send(final byte[] data, long timeout, TimeUnit timeUnit) throws IOException {

        long requestId = IDGenerator.nextId();
        long milTimeout = timeUnit.toMillis(timeout);
        RequestCallBack callback = new LapseRequestCallBack(this.session,requestId, milTimeout);
        TimerTask timeTask = new TimerTask(callback, milTimeout);
        ((LapseActor) this.session.getAttachObject(Constant.ACTOR_ATTRIBUTE)).addTimerTask(requestId, timeTask);
        if(this.session.addCallBack(requestId, callback) != null){
            throw new IllegalStateException("请求Id已经存在！");
        }
        LapseMessage msg = new LapseMessage(requestId, data, Command.REQUEST);
        session.write(msg);
        return callback.getSendResult();
    }


    @Override
    public void response(long requestId, byte[] data) throws IOException {
        if (data == null) {
            data = "success！".getBytes();
        }
        LapseMessage msg = new LapseMessage(requestId, data, Command.RESPONSE);
        this.session.write(msg);
    }

}
