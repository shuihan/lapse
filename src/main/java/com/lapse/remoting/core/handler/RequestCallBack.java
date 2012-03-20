package com.lapse.remoting.core.handler;

import com.lapse.remoting.client.SendResult;


/**
 * ������Ϣ�ص�����
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
