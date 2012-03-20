package com.lapse.remoting.core.impl;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.lapse.remoting.client.SendResult;
import com.lapse.remoting.config.LapseConfig;
import com.lapse.remoting.core.Connection;
import com.lapse.remoting.core.handler.MessageReceiveListener;
import com.lapse.remoting.message.LapseMessage;

public class LapseTCPServerTest {

    public static void main(String[] args){
        LapseConfig config = new LapseConfig();
        LapseTCPServer server = new LapseTCPServer(config,new MessageReceiveListener() {
            
            @Override
            public void receivedMessage(Connection connection, LapseMessage message) {
                System.out.println("收到消息="+new String(message.getData()));
                try {
                    connection.response(message.getRequestId(), null);
                    SendResult result = connection.send("服务器已经收到消息！".getBytes(),10,TimeUnit.SECONDS);
                    System.out.println("服务器发送消息"+ result.getResult());
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            
            
            @Override
            public Executor getExecutor() {
                return Executors.newCachedThreadPool();
            }
        });
        server.start();
    }
}
