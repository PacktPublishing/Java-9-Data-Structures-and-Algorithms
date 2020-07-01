package com.example.reactive;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by debasishc on 24/1/17.
 */
public class ThreadSafeCounter {
    AtomicInteger counter;
    public int incrementAndGet(){
        while (true){
            int value = counter.get();
            if(counter.compareAndSet(value, value+1)){
                return value;
            }
        }
    }
}
