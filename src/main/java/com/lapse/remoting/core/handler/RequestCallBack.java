package com.lapse.remoting.core.handler;

import com.lapse.remoting.client.SendResult;


/**
 * 发送消息回调对象
 * 
 * @author Administrator
 * 
 */
public interface RequestCallBack {

    public void onResponse(long requestId, byte[] data);


    public void onException(long requestId, Result result, String reason);


    public void onException(long requestId, Result result);

    public enum Result {
        Timeout,
        Exception,
        Success;
    }


    public SendResult getSendResult();

}
