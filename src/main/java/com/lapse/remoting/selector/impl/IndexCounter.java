package com.lapse.remoting.selector.impl;

import java.util.concurrent.atomic.AtomicInteger;

public class IndexCounter {

	private AtomicInteger reference = new AtomicInteger();
	
	public int getIndex(){
		
		int index = Math.abs(reference.getAndAdd(1));
		
		return index;
	}
}
