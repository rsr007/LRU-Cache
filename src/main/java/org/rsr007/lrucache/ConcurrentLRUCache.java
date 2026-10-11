package org.rsr007.lrucache;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.SequencedMap;

public class ConcurrentLRUCache <K, V> {
    final int capacity;
    final SequencedMap<K, V> map;

    public ConcurrentLRUCache(final int cap) {
        if(cap<=0){
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        capacity = cap;
        //remember u have to provide contructor params as below, it cannot be simply LinkedhashMap
                /*
                1. It tracks "Insertion Order", not "Access Order": By default, LinkedHashMap only tracks the order in which items are added. If you insert [A, B, C], A stays at the front forever—even if your program reads A a million times. An LRU Cache must move A to the back whenever it is accessed so it isn't wrongfully evicted. Passing true for the third parameter (accessOrder) fixes this behavior.
                2. Resizing Overhead (capacity + 1):By default, when a map reaches 75% of its capacity, it resizes (doubles in size), which takes \(O(N)\) time. In a fixed-capacity LRU cache, you want to completely prevent resizing since you are manually keeping the map at a maximum size by evicting items. Setting the initial capacity to capacity + 1 ensures the map never needs to resize.
                 */
        map = new LinkedHashMap<>(capacity + 1, 0.75f, true) {
        };
    }

    //Normally, "reading" from a data structure is a passive operation.
    // However, because our map is tracking access order, a get() call is actually a secret write operation.
    //It manipulates the pointer nodes in its internal doubly linked list to move the element to the end of the list (Write).
    public V get(K key) {
        synchronized (map) {
            return map.get(key);
        }
    }

    public void put(K key, V value) {
        synchronized (map) {
            if (!map.containsKey(key) && map.size() == capacity) {
                map.pollFirstEntry();
            }
            map.put(key, value);
        }
    }

    public void clear() {
        synchronized (map) {
            map.clear();
        }
    }

    public int size() {
        synchronized (map) {
            return map.size();
        }
    }

    public SequencedMap<K,V> getAllData(){
        synchronized (map){
            return Collections.unmodifiableSequencedMap(new LinkedHashMap<>(map));
        }
    }
}
