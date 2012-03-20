package com.lapse.remoting.core.handler;

import java.util.concurrent.Executor;

import com.lapse.remoting.core.Connection;
import com.lapse.remoting.message.LapseMessage;


public interface MessageReceiveListener {

    public void receivedMessage(Connection connection, LapseMessage message);


    public Executor getExecutor();
}
