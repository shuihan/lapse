package com.lapse.remoting.core;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import com.lapse.remoting.core.handler.Handler.SocketEvent;
import com.lapse.remoting.core.impl.SelectorManager;
import com.lapse.remoting.util.TimerQueue;


/**
 * actor response of many client's register and manager
 * 
 * @author shuihan
 * 
 */
public class LapseActor extends Thread {

    private Selector selector;

    private SelectorManager selectorManager;

    private final TimerQueue<Long, TimerTask> queue = new TimerQueue<Long, TimerTask>();


    public LapseActor(SelectorManager selectorManager) throws IOException {
        this.selectorManager = selectorManager;
        this.selector = Selector.open();
    }


    public TimerTask addTimerTask(long id, TimerTask task) {
        return this.queue.add(id, task);
    }


    protected void visit() {
        for (Entry<Long, TimerTask> entry : this.queue.getEntitys()) {
            if (entry.getValue().isTimeOut()) {
                entry.getValue().execute();
            }
        }
    }


    public TimerTask removeTask(long id) {
        return this.queue.remove(id);
    }


    public Selector getSelector() {
        return this.selector;
    }


    @Override
    public void run() {
        if (!selectorManager.isInit()) {
            return;
        }
        while (true) {
            try {
                int selectedKeyCount = this.selector.select(1);
                if (selectedKeyCount <= 0) {
                    continue;
                }
                Set<SelectionKey> selectedKeys = this.selector.selectedKeys();
                this.processKeys(selectedKeys);
                this.visit();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
            }
        }
    }


    public void registerChannel(SelectableChannel channel, int ops, Object object) throws IOException {
        if (this.selector != null && channel.isOpen()) {
            channel.register(this.selector, ops, object);
        }
        else {
            channel.close();
        }
    }


    public void wakeUp() {
        if (this.selector != null) {
            this.selector.wakeup();
        }
    }


    private void processKeys(Set<SelectionKey> keys) throws IOException {
        Iterator<SelectionKey> it = keys.iterator();
        while (it.hasNext()) {
            SelectionKey key = it.next();
            it.remove();
            if (key.isAcceptable()) {
                this.selectorManager.getRemotingController().handleSocketEvent(key, SocketEvent.Accept);
            }
            else if (key.isReadable()) {
                this.selectorManager.getRemotingController().handleSocketEvent(key, SocketEvent.Read);
            }
            else if (key.isWritable()) {
                this.selectorManager.getRemotingController().handleSocketEvent(key, SocketEvent.Write);
            }
            else if (key.isConnectable()) {
                this.selectorManager.getRemotingController().handleSocketEvent(key, SocketEvent.Connect);
            }
        }
    }
}
