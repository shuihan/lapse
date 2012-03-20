package com.lapse.remoting.util;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class TimerQueue<K,V> {

    private final ConcurrentHashMap<K,V> queue = new ConcurrentHashMap<K,V>();
    
    
    public V add(K key,V value){
        return this.queue.putIfAbsent(key, value);
    }
   
    
    public V remove(K key){
        return this.queue.remove(key);
    }
    
    public Set<Entry<K,V>> getEntitys(){
       return this.queue.entrySet();
    }
}
