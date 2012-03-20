package com.lapse.remoting.util;

public enum Command {

    REQUEST,
    RESPONSE;

    public static byte getByte(Command comm) {
        switch (comm) {
        case REQUEST:
            return Constant.REQUEST;
        case RESPONSE:
            return Constant.RESPONSE;
        default:
            return Constant.REQUEST;
        }
    }


    public static Command getCommandByByte(byte data) {
        if(data == Constant.REQUEST){
            return REQUEST;
        }
        else if(data == Constant.RESPONSE){
            return RESPONSE;
        }else
        {
            throw new IllegalArgumentException("非法参数 "+data);
        }
    }
}
