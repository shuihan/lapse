package com.lapse.remoting.selector.impl;

import java.util.List;
import java.util.Random;

import com.lapse.remoting.core.LapseActor;
import com.lapse.remoting.selector.ActorSelector;

public class RandomActorSelector implements ActorSelector {

	private final Random random = new Random();
	
	public LapseActor getLapseActor(List<LapseActor> lapseActors) {
		if(lapseActors == null){
			return null;
		}
		return lapseActors.get(random.nextInt(lapseActors.size()));
	}

}
