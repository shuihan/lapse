package com.lapse.remoting.config;

public class LapseConfig {

    // 服务器端口
    private int port = 7777;

    // 配置是否阻塞
    private boolean isBlock = false;

    // 超时时间
    private int timeout = 1000;

    // 长短连接设置
    private boolean keepLive = true;

    // 发送缓冲区大小
    private int sendBuffSize = 1024;

    // 接收缓冲区大小
    private int receiveBuffSize = 1024;

    // 地址是否重用
    private boolean reuseAddress = true;

    // 是否缓冲延时
    private boolean isTcpDelay = false;

    // 是否接收紧急数据，即频道外数据
    private boolean receiveUrgentData = false;

    // 是否允许关闭后停留
    private boolean isSoLinger = true;

    // 关闭后停留的时间，单位s
    private int soLingerTimeOut = 2;

    // 是否允许广播
    private boolean allowBroadcast = false;

    private int corePoolSize = 10;

    private int maxPoolSize = 30;

    private long liveTime = 60;


    public int getCorePoolSize() {
        return corePoolSize;
    }


    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }


    public int getMaxPoolSize() {
        return maxPoolSize;
    }


    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }


    public long getLiveTime() {
        return liveTime;
    }


    public void setLiveTime(long liveTime) {
        this.liveTime = liveTime;
    }


    public boolean isKeepLive() {
        return keepLive;
    }


    public void setKeepLive(boolean keepLive) {
        this.keepLive = keepLive;
    }


    public int getTimeout() {
        return timeout;
    }


    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }


    public int getPort() {
        return port;
    }


    public void setPort(int port) {
        this.port = port;
    }


    public boolean isBlock() {
        return isBlock;
    }


    public void setBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }


    public int getSendBuffSize() {
        return sendBuffSize;
    }


    public void setSendBuffSize(int sendBuffSize) {
        this.sendBuffSize = sendBuffSize;
    }


    public int getReceiveBuffSize() {
        return receiveBuffSize;
    }


    public void setReceiveBuffSize(int receiveBuffSize) {
        this.receiveBuffSize = receiveBuffSize;
    }


    public boolean isReuseAddress() {
        return reuseAddress;
    }


    public void setReuseAddress(boolean reuseAddress) {
        this.reuseAddress = reuseAddress;
    }


    public boolean isTcpDelay() {
        return isTcpDelay;
    }


    public void setTcpDelay(boolean isTcpDelay) {
        this.isTcpDelay = isTcpDelay;
    }


    public boolean isReceiveUrgentData() {
        return receiveUrgentData;
    }


    public void setReceiveUrgentData(boolean receiveUrgentData) {
        this.receiveUrgentData = receiveUrgentData;
    }


    public boolean isSoLinger() {
        return isSoLinger;
    }


    public void setSoLinger(boolean isSoLinger) {
        this.isSoLinger = isSoLinger;
    }


    public int getSoLingerTimeOut() {
        return soLingerTimeOut;
    }


    public void setSoLingerTimeOut(int soLingerTimeOut) {
        this.soLingerTimeOut = soLingerTimeOut;
    }


    public boolean isAllowBroadcast() {
        return allowBroadcast;
    }


    public void setAllowBroadcast(boolean allowBroadcast) {
        this.allowBroadcast = allowBroadcast;
    }

}
