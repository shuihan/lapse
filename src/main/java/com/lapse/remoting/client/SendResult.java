package com.lapse.remoting.client;

import com.lapse.remoting.core.handler.RequestCallBack.Result;


public class SendResult {

    private Result result;

    private String reason;

    private byte[] data;


    public SendResult(Result result, String reason, byte[] data) {
        this.reason = reason;
        this.result = result;
        this.data = data;
    }


    public Result getResult() {
        return result;
    }


    public void setResult(Result result) {
        this.result = result;
    }


    public String getReason() {
        return reason;
    }


    public void setReason(String reason) {
        this.reason = reason;
    }


    public byte[] getData() {
        return data;
    }


    public void setData(byte[] data) {
        this.data = data;
    }
}
