package com.lapse.remoting.selector.impl;

import java.util.List;

import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.selector.ActorSelector;


public class RoundActorSelector implements ActorSelector {

    private final IndexCounter indexCounter = new IndexCounter();


    public LapseActor getLapseActor(List<LapseActor> lapseActors) {
        if (lapseActors == null) {
            return null;
        }
        int index = this.indexCounter.getIndex();

        return lapseActors.get(index % lapseActors.size());
    }

}
