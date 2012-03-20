package com.lapse.remoting.util;

public class Constant {

    public static final String ACTOR_ATTRIBUTE = "ACTOR_ATTRIBUTE";

    public static final String DEFAULT_GROUP = "DEFAULT_GROUP";

    public static final String CONNECT_LOCK = "CONNECT_LOCK";

    public static final byte magic = 0x01;

    public static final byte REQUEST = 0x02;

    public static final byte RESPONSE = 0x03;

    // 1字节魔数+4字节消息长度+8字节响应id+4字节消息校验+1字节命令请求
    public static final int HEADER_LENGTH = 1 + 4 + 8 + 4+1;
}
