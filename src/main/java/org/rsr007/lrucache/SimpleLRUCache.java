package org.rsr007.lrucache;

import java.util.SequencedCollection;
import java.util.SequencedMap;

public class SimpleLRUCache<T> {

    private final ConcurrentLRUCache<T,T> lruCache;

    /**
     * Remember Cache is never dynamically resized, here we taking input from customer
     * else in real world it has fixed static size
     * @param capacity capacity of cache being used
     */
    public SimpleLRUCache(int capacity) {
        lruCache = new ConcurrentLRUCache<>(capacity);
    }

    public void add(T element){
        lruCache.put(element,element);
    }

    public T get(T element){
        return lruCache.get(element);
    }

    public int size(T element){
        return lruCache.size();
    }

    public void clear(){
        lruCache.clear();
    }

    //Here abstraction, instead of map<key,key>, we return sequencedValues or sequencedKeySet
    public SequencedCollection<T> getAllData(){
        return lruCache.getAllData().sequencedKeySet();
    }
}
