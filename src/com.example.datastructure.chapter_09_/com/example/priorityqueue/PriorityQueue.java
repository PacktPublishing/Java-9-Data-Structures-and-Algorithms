package com.example.priorityqueue;

import com.example.adt.OrderedStore;

/**
 * Created by debasishc on 7/11/16.
 */
public interface PriorityQueue<E> extends OrderedStore<E>{
    E checkMinimum();
    E dequeueMinimum();
    void enqueue(E value);

    @Override
    default E checkFirst(){
        return checkMinimum();
    }

    @Override
    default void insert(E value){
        enqueue(value);
    }

    @Override
    default E pickFirst(){
        return dequeueMinimum();
    }
}
