package com.example.adt;

/**
 * Created by debasishc on 22/12/16.
 */
public interface OrderedStore<E> extends Iterable<E>{
    void insert(E value);
    E pickFirst();
    E checkFirst();
}
