package com.lapse.remoting.core.impl;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.ArrayList;
import java.util.List;

import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.core.RemotingContext;
import com.lapse.remoting.core.handler.MessageReceiveListener;
import com.lapse.remoting.core.handler.RemotingController;
import com.lapse.remoting.core.handler.impl.LapseRemotingController;
import com.lapse.remoting.selector.ActorSelector;
import com.lapse.remoting.selector.impl.RoundActorSelector;


/**
 * 
 * @author shuihan
 * 
 */
public class SelectorManager {

    private volatile boolean isInit = false;

    private final ActorSelector actorSelector;

    private RemotingContext remotingContext;

    private RemotingController remotingController;

    private static int ActorCount = Runtime.getRuntime().availableProcessors() / 2 - 1;

    static {
        if (ActorCount <= 0) {
            ActorCount = 1;
        }
    }

    private List<LapseActor> actors = null;


    public SelectorManager(RemotingContext remotingContext, MessageReceiveListener messageReceiveListener) {
        this.actorSelector = new RoundActorSelector();
        this.remotingContext = remotingContext;
        this.remotingController = new LapseRemotingController(remotingContext, this, messageReceiveListener);
        this.init(ActorCount);
    }


    public void setMessageReceiveListener(MessageReceiveListener messageReceiveListener) {
        this.remotingController.setMessageReceiveListener(messageReceiveListener);
    }


    private void init(int actorCount) {
        actors = new ArrayList<LapseActor>();
        try {
            for (int i = 0; i < actorCount; i++) {
                LapseActor actor = new LapseActor(this);
                actors.add(actor);
                actor.start();
            }
            isInit = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            isInit = false;
        }
    }


    public LapseActor getActor() {
        return this.actorSelector.getLapseActor(actors);

    }


    public SelectionKey getKeyForChannel(SelectableChannel channel) {
        for (LapseActor actor : this.actors) {
            SelectionKey key = channel.keyFor(actor.getSelector());
            if (key != null) {
                return key;
            }
        }

        return null;
    }


    public LapseActor getActor(int index) {
        if (!this.isInit) {
            return null;
        }
        if (index > ActorCount) {
            throw new ArrayIndexOutOfBoundsException("the actor count=" + ActorCount);
        }
        return this.actors.get(index);
    }


    public List<LapseActor> getActors() {
        return this.actors;
    }


    public boolean isInit() {
        return isInit;
    }


    public RemotingContext getRemotingContext() {
        return remotingContext;
    }


    public void setRemotingContext(RemotingContext remotingContext) {
        this.remotingContext = remotingContext;
    }


    public RemotingController getRemotingController() {
        return remotingController;
    }


    public void setRemotingController(RemotingController remotingController) {
        this.remotingController = remotingController;
    }


    public ActorSelector getActorSelector() {
        return actorSelector;
    }
}
