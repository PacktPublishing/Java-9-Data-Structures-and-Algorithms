package com.example.adt;

/**
 * Created by debasishc on 12/8/16.
 */
public interface DoubleEndedQueue<E> extends Stack<E> {
    void inject(E value);
    E eject();
    E peekLast();
}
