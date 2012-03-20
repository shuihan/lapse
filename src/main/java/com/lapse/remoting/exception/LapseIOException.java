package com.lapse.remoting.exception;

import java.io.IOException;


public class LapseIOException extends IOException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;


    public LapseIOException() {
        super();
    }


    public LapseIOException(String msg) {
        super(msg);
    }


    public LapseIOException(String msg, Throwable e) {
        super(msg, e);
    }


    public LapseIOException(Throwable e) {
        super(e);
    }

}
