package org.rsr007.lrucache;

public class Application {

    public static void main(String[] args) {
        //create map capacity is 3
        SimpleLRUCache<String> cache = new SimpleLRUCache<>(3);

        cache.add("ravi");
        cache.add("shankar");
        cache.add("rai");
        System.out.println("Initial state: "+cache.getAllData()); //note order in map here

        cache.get("ravi");

        //check order in map, it has changed and "ravi" is pushed to last in linkedlist
        System.out.println("Post access [ravi]: "+ cache.getAllData());

        //now if i put new element, it will evict first and add it to last (since latest item)
        cache.add("shiva");
        System.out.println("Post eviction nd add [shiva]: "+ cache.getAllData());
    }

}

