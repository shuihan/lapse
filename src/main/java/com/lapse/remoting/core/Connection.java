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
     * send one way ������Ϣ
     * 
     * @param data
     */
    public void sendOneWay(byte[] data) throws IOException;


    /**
     * ͬ������
     * 
     * @param data
     * @param timeout
     * @param timeUnit
     * @throws IOException
     */
    public SendResult send(byte[] data, long timeout, TimeUnit timeUnit) throws IOException;


    /**
     * ͬ������
     * 
     * @param data
     * @throws IOException
     */
    public SendResult send(byte[] data) throws IOException;


    /**
     * ��Ӧ��Ϣ��
     * 
     * @param requestId
     * @param data
     * @throws IOException
     */
    public void response(long requestId, byte[] data) throws IOException;


    Session getSession();

}
