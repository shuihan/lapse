package com.lapse.remoting.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

import com.lapse.remoting.core.Connection;
import com.lapse.remoting.core.RemotingContext;
import com.lapse.remoting.core.Session;
import com.lapse.remoting.manager.TransportManager;


public class LapseRemotingContext implements RemotingContext {

    private ConcurrentHashMap<String, List<Connection>> connections = new ConcurrentHashMap<String, List<Connection>>();

    private ConcurrentHashMap<Session, Connection> session2Connection = new ConcurrentHashMap<Session, Connection>();

    private TransportManager transportManager;


    public LapseRemotingContext(TransportManager transportManager) {
        this.transportManager = transportManager;
    }


    public TransportManager getTransportManager() {
        return transportManager;
    }


    public void setTransportManager(TransportManager transportManager) {
        this.transportManager = transportManager;
    }


    public synchronized void registerConnection(String group, Connection connection) {
        List<Connection> oldConnections = this.connections.get(group);
        if (oldConnections == null) {
            oldConnections = new ArrayList<Connection>();
            this.connections.put(group, oldConnections);
        }
        oldConnections.add(connection);
        this.session2Connection.put(connection.getSession(), connection);
    }


    public synchronized void unRegisterConnection(String group, Connection connection) {
        List<Connection> oldConnection = this.connections.get(group);
        if (oldConnection != null) {
            oldConnection.remove(connection);
        }
        this.session2Connection.remove(connection.getSession());
    }


    @Override
    public synchronized void unRegisterAllConnection(String group) {
        List<Connection> conns = this.connections.remove(group);
        for (Connection conn : conns) {
            this.session2Connection.remove(conn.getSession());
        }
    }


    @Override
    public ThreadPoolExecutor getTransportThreadPool() {

        if (this.transportManager != null) {
            return this.transportManager.getRemotingThreadPool();
        }

        return null;
    }


    @Override
    public List<Connection> getConnectionByGroup(String group) {
        return this.connections.get(group);
    }


    @Override
    public Connection getConnectionBySession(Session session) {
        return this.session2Connection.get(session);
    }

}
