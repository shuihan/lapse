package com.lapse.remoting.exception;

public class LapseIllegalArgumentException extends IllegalArgumentException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;


    public LapseIllegalArgumentException() {
        super();
    }


    public LapseIllegalArgumentException(String msg) {
        super(msg);
    }


    public LapseIllegalArgumentException(String msg, Throwable e) {
        super(msg, e);
    }

}