package com.lapse.remoting.message;

import java.nio.ByteBuffer;

import com.lapse.remoting.util.CRC32Util;
import com.lapse.remoting.util.Command;
import com.lapse.remoting.util.Constant;


/**
 * 1�ֽ�ħ��+4�ֽ���Ϣ����+8�ֽ���Ӧid+4�ֽ���ϢУ��+��Ϣ��body
 * 
 * @author shuihan
 * 
 */
public class LapseMessage {

    private long requestId;

    private byte[] data;

    private Command commond;


    public LapseMessage(long requestId, byte[] data, Command commond) {
        this.requestId = requestId;
        if (data == null) {
            throw new IllegalArgumentException("data can not be null!");
        }
        this.data = data;
        this.commond = commond;
    }


    public ByteBuffer encode() {
        ByteBuffer buffer = ByteBuffer.allocate(Constant.HEADER_LENGTH + this.data.length);
        buffer.put(Constant.magic);
        buffer.putInt(data.length);
        buffer.putLong(this.requestId);
        buffer.putInt(CRC32Util.crc32(this.data));
        buffer.put(Command.getByte(commond));
        buffer.put(this.data);
        buffer.flip();
        return buffer;
    }


    public LapseMessage() {

    }


    public Command getCommond() {
        return commond;
    }


    public byte[] getData() {
        return data;
    }


    public long getRequestId() {
        return requestId;
    }

}
