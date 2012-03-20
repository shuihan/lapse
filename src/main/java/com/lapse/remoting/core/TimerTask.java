package com.lapse.remoting.core;

import com.lapse.remoting.core.handler.RequestCallBack;


public class TimerTask {

    private long taskGenerateTime;

    private long timeout;

    private long timeoutTime;


    public TimerTask(RequestCallBack requestCallBack, long timeout) {
        this.timeout = timeout;
        this.taskGenerateTime = System.currentTimeMillis();
        this.timeoutTime = this.taskGenerateTime + this.timeout;
    }


    public boolean isTimeOut() {
        if (System.currentTimeMillis() >= this.timeoutTime) {
            return true;
        }
        else {
            return false;
        }
    }


    public void execute() {

    }

}
