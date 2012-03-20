package com.lapse.remoting.client.impl;

import com.lapse.remoting.config.LapseConfig;


public class LapseClientConfig extends LapseConfig {

    private String ip = "127.0.0.1";


    public String getIp() {
        return ip;
    }


    public void setIp(String ip) {
        this.ip = ip;
    }

}
