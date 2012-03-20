package com.lapse.remoting.selector;

import java.util.List;

import com.lapse.remoting.core.LapseActor;

public interface ActorSelector {

	public LapseActor getLapseActor(List<LapseActor> lapseActors);
}
