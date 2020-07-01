package com.example.adt;

/**
 * Created by debasishc on 12/8/16.
 */
public interface Stack<E> extends OrderedStore<E>{
    void push(E value);
    E pop();
    E peek();
    @Override
    default E checkFirst(){
        return peek();
    }

    @Override
    default void insert(E value){
        push(value);
    }

    @Override
    default E pickFirst(){
        return pop();
    }
}
