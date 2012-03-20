package com.lapse.remoting.core.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.lapse.remoting.core.Session;

public class SessionManager {

	private ConcurrentHashMap<String,List<Session>> sessions = new ConcurrentHashMap<String,List<Session>>();
	
	public void registerSession(String group,Session session){
		List<Session> groupSession = this.sessions.get(group);
		 if(groupSession == null){
			 List<Session> newGroupSession = new ArrayList<Session>();
			 newGroupSession.add(session);
			 this.sessions.put(group, newGroupSession);
			 groupSession = newGroupSession;
		 }else
		 {
			 groupSession.add(session);
		 }
	}
	
	
	public void removeSession(String group,Session session){
		List<Session> groupSession = this.sessions.get(group);
		if(groupSession == 	null){
			return ;
		}
		groupSession.remove(session);
	}
	
	
	public List<Session> getSessions(String group){
		return this.sessions.get(group);
	}
}
