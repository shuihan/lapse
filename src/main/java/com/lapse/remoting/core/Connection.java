package com.lapse.remoting.core;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import com.lapse.remoting.client.SendResult;


/**
 * the high abstract of session ,can be invoke by the POJO, instead of one
 * client.
 * 
 * @author Administrator
 * 
 */
public interface Connection {

    /**
     * send one way 发送消息
     * 
     * @param data
     */
    public void sendOneWay(byte[] data) throws IOException;


    /**
     * 同步发送
     * 
     * @param data
     * @param timeout
     * @param timeUnit
     * @throws IOException
     */
    public SendResult send(byte[] data, long timeout, TimeUnit timeUnit) throws IOException;


    /**
     * 同步发送
     * 
     * @param data
     * @throws IOException
     */
    public SendResult send(byte[] data) throws IOException;


    /**
     * 响应消息方
     * 
     * @param requestId
     * @param data
     * @throws IOException
     */
    public void response(long requestId, byte[] data) throws IOException;


    Session getSession();

}
