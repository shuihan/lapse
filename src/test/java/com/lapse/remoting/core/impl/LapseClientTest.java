package com.lapse.remoting.core.impl;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.lapse.remoting.client.LapseClient;
import com.lapse.remoting.client.SendResult;
import com.lapse.remoting.client.impl.DefaultLapseClient;
import com.lapse.remoting.client.impl.LapseClientConfig;
import com.lapse.remoting.core.Connection;
import com.lapse.remoting.core.handler.MessageReceiveListener;
import com.lapse.remoting.message.LapseMessage;
import com.lapse.remoting.util.Constant;


public class LapseClientTest {

    public static void main(String[] args) throws IOException, InterruptedException, TimeoutException {
        LapseClient client = new DefaultLapseClient(new LapseClientConfig());
        client.setMessageReceiveListener(new MessageReceiveListener() {

            @Override
            public void receivedMessage(Connection connection, LapseMessage message) {
                System.out.println("客户端收到消息=" + new String(message.getData()));
                try {
                    connection.response(message.getRequestId(),null);
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
        client.connect();
        client.waitForConnectionReady(1000);
        sendMessage(client);
        sendOneWay(client);
    }


    private static void sendMessage(LapseClient client) {
        List<Connection> connections = client.getRemotingContext().getConnectionByGroup(Constant.DEFAULT_GROUP);
        if (connections == null || connections.isEmpty()) {
            System.err.println("no connection!");
            return;
        }
        Connection conn = connections.iterator().next();
        try {
            SendResult result = conn.send("hello!".getBytes(), 3000, TimeUnit.MILLISECONDS);
            System.out.println("SendResult=" + result.getResult() + ",reason=" + result.getReason());
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void sendOneWay(LapseClient client) throws IOException {
        List<Connection> connections = client.getRemotingContext().getConnectionByGroup(Constant.DEFAULT_GROUP);
        if (connections == null || connections.isEmpty()) {
            System.err.println("no connection!");
            return;
        }
        Connection conn = connections.iterator().next();
        conn.sendOneWay("sendOneWay！".getBytes());
    }
}
