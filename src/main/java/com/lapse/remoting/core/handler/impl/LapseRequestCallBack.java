package com.lapse.remoting.core.handler.impl;

import com.lapse.remoting.client.SendResult;
import com.lapse.remoting.core.Session;
import com.lapse.remoting.core.handler.RequestCallBack;


public class LapseRequestCallBack implements RequestCallBack {

    private long requestId;

    private final Object lock = new Object();

    private long timeout;

    private boolean isCompleted = false;

    private SendResult sendResult = null;

    private Session session;


    public LapseRequestCallBack(Session session, long requestId, long timeout) {
        this.requestId = requestId;
        this.timeout = timeout;
        this.session = session;
    }


    @Override
    public void onResponse(long requestId, byte[] data) {
        this.tryComplete(requestId, Result.Success, null, data);
    }


    @Override
    public void onException(long requestId, Result result, String reason) {
        this.tryComplete(requestId, result, reason, null);
    }


    @Override
    public void onException(long requestId, Result result) {
        this.tryComplete(requestId, result, null, null);
    }


    private void tryComplete(long requestId, Result result, String reason, byte[] data) {
        synchronized (lock) {
            if (this.requestId == requestId) {
                this.sendResult = new SendResult(result, reason, data);
                //移除回调
                this.removeCallback(requestId);
                this.lock.notifyAll();
            }
        }
    }


    @Override
    public SendResult getSendResult() {
        synchronized (lock) {
            long start = System.currentTimeMillis();
            if (!this.isCompleted) {
                try {
                    this.lock.wait(this.timeout);
                }
                catch (InterruptedException e) {
                    Thread.interrupted();
                }
            }
            long end = System.currentTimeMillis();
            if ((end - start) >= timeout) {
                this.sendResult = new SendResult(Result.Timeout, "发送消息超时,timeout=" + timeout, null);
                //注意移除回调
                this.removeCallback(this.requestId);
            }
            
            return sendResult;
        }

    }
    
    
    private void removeCallback(long requestId){
        this.session.removeCallBack(requestId);
    }

}
