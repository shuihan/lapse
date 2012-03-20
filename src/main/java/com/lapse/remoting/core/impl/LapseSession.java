package com.lapse.remoting.core.impl;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.core.Session;
import com.lapse.remoting.core.handler.RemotingController;
import com.lapse.remoting.core.handler.RequestCallBack;
import com.lapse.remoting.exception.LapseIOException;
import com.lapse.remoting.message.LapseMessage;
import com.lapse.remoting.util.CRC32Util;
import com.lapse.remoting.util.Command;
import com.lapse.remoting.util.Constant;


public class LapseSession implements Session {

    private SelectableChannel channel;

    private ByteBuffer buffer;

    private final int bufferLength = 16 * 1024;

    private SocketAddress socketAddress;

    private long lastTime;

    private final Queue<LapseMessage> writeQueue = new LinkedBlockingQueue<LapseMessage>();

    private final Map<String, Object> attatchAttribute = new ConcurrentHashMap<String, Object>();

    private final ConcurrentHashMap<Long, RequestCallBack> callbacks = new ConcurrentHashMap<Long, RequestCallBack>();

    private boolean isClose = true;

    // 当前正在要写的数据
    private AtomicReference<LapseMessage> writeMessage = new AtomicReference<LapseMessage>();

    private RemotingController remotingController;


    @Override
    public RequestCallBack addCallBack(long requestId, RequestCallBack callback) {
        return this.callbacks.putIfAbsent(requestId, callback);
    }


    @Override
    public RequestCallBack removeCallBack(long requestId) {
        return this.callbacks.remove(requestId);
    }


    public int hashCode() {
        int result = 1;
        result = result * 31 + (this.isClose ? 0 : 1);
        // result = result * 31 + (int) (this.lastTime ^ (this.lastTime >>>
        // 32));

        return result;
    }


    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        if (this == object) {
            return true;
        }
        LapseSession other = (LapseSession) object;
        if (this.isClose != other.isClose) {
            return false;
        }

        return true;
    }


    public static LapseSession buildSession(SelectableChannel selectableChannel, RemotingController remotingController)
            throws LapseIOException {
        return new LapseSession(selectableChannel, remotingController);
    }


    public LapseSession(SelectableChannel selectableChannel, RemotingController remotingController)
            throws LapseIOException {
        this.channel = selectableChannel;
        if (this.channel instanceof SocketChannel) {
            this.socketAddress = ((SocketChannel) this.channel).socket().getRemoteSocketAddress();
        }
        this.remotingController = remotingController;
        this.buffer = ByteBuffer.allocate(this.bufferLength);
        this.isClose = false;
    }


    public void attachObject(String key, Object object) {
        this.attatchAttribute.put(key, object);
    }


    public Object getAttachObject(String key) {
        return this.attatchAttribute.get(key);
    }


    public SocketAddress getClientSocketAddress() {
        return socketAddress;
    }


    @Override
    public Object removeAttachObject(String key) {
        return this.attatchAttribute.remove(key);
    }


    @Override
    public void onRead(Selector selector) throws IOException {
        ReadableByteChannel readChannel = (ReadableByteChannel) this.channel;
        while (readChannel.isOpen() && readChannel.read(this.buffer) > 0) {
            if (!this.buffer.hasRemaining()) {
                //TODO 增大buf
                
                break;
            }
        }
        this.buffer.flip();
        // 如果buffer剩余数少于HEADER_LENGTH个字节，则等待下一次读取。
        if (this.buffer.remaining() < Constant.HEADER_LENGTH) {
            return;
        }
        byte magic = this.buffer.get();
        // 如果不是本通讯协议，则关闭连接
        if (magic != Constant.magic) {
            this.close();
        }
        int msgLength = this.buffer.getInt();
        // 读取的消息长度为负数应该出错了，直接关闭连接
        if (msgLength < 0) {
            this.close();
            return;
        }
        // 请求id
        long requestId = this.buffer.getLong();

        int crcValue = this.buffer.getInt();
        // 请求命令
        byte command = this.buffer.get();
        // 解析出数据
        byte[] body = new byte[msgLength];
        this.buffer.get(body);
        if (crcValue != CRC32Util.crc32(body)) {
            this.close();
        }
        // 压缩buffer
        this.buffer.compact();

        //TODO
        System.out.println("read data="+new String(body));
        
        LapseMessage msg = new LapseMessage(requestId, body, Command.getCommandByByte(command));
        this.remotingController.onMessageArrived(this, msg);
        this.updateLastTime();
    }


    @Override
    public void onWrite(Selector selector) throws IOException {
        this.getMessage();
        WritableByteChannel writableChannel = (WritableByteChannel) this.channel;
        while (writableChannel.isOpen() && this.writeMessage.get() != null) {
            ByteBuffer buffer = writeMessage.get().encode();
            while(buffer.hasRemaining()){
                writableChannel.write(buffer);
            }
            System.out.println("write data="+new String(writeMessage.get().getData()));
            this.getMessage();
        }
        this.updateLastTime();
    }


    private void getMessage() {
        LapseMessage message = this.writeQueue.poll();
        this.writeMessage.set(message);
    }


    private void updateLastTime() {
        this.lastTime = System.currentTimeMillis();
    }


    @Override
    public void close() {
        try {
            Socket socket = ((SocketChannel) this.channel).socket();
            socket.getInputStream().close();
            socket.close();
            this.isClose = true;
        }
        catch (IOException e) {
            // ignore
            this.isClose = true;
        }
    }


    public long getLastTime() {
        return lastTime;
    }


    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }


    @Override
    public boolean isClose() {
        return this.isClose;
    }


    @Override
    public boolean syncWrite(byte[] data) throws LapseIOException {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    public boolean write(LapseMessage message) throws LapseIOException {
        boolean result = this.writeQueue.offer(message);
        if (result) {
            SelectionKey key =
                    this.channel.keyFor(((LapseActor) this.getAttachObject(Constant.ACTOR_ATTRIBUTE)).getSelector());
            key.interestOps(key.interestOps() | SelectionKey.OP_WRITE);
        }
        return result;
    }


    @Override
    public SelectableChannel getChannel() {
        return this.channel;
    }


    @Override
    public String toString() {
        String result = "remote: " + (this.socketAddress == null ? null : this.socketAddress.toString());
        result += "\nlocal: " + this.getLocalAddress();
        return result;
    }


    @Override
    public SocketAddress getRemotingSocketAddress() {
        return this.socketAddress;
    }


    @Override
    public SocketAddress getLocalAddress() {
        return ((SocketChannel) this.channel).socket().getLocalSocketAddress();
    }


    @Override
    public RequestCallBack getCallBack(long requestId) {
        return this.callbacks.get(requestId);
    }
}
