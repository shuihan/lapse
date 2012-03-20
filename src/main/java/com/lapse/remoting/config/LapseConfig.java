package com.lapse.remoting.config;

public class LapseConfig {

    // �������˿�
    private int port = 7777;

    // �����Ƿ�����
    private boolean isBlock = false;

    // ��ʱʱ��
    private int timeout = 1000;

    // ������������
    private boolean keepLive = true;

    // ���ͻ�������С
    private int sendBuffSize = 1024;

    // ���ջ�������С
    private int receiveBuffSize = 1024;

    // ��ַ�Ƿ�����
    private boolean reuseAddress = true;

    // �Ƿ񻺳���ʱ
    private boolean isTcpDelay = false;

    // �Ƿ���ս������ݣ���Ƶ��������
    private boolean receiveUrgentData = false;

    // �Ƿ�����رպ�ͣ��
    private boolean isSoLinger = true;

    // �رպ�ͣ����ʱ�䣬��λs
    private int soLingerTimeOut = 2;

    // �Ƿ�����㲥
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
