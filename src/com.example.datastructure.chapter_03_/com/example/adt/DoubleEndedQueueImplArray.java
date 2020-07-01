package com.example.adt;

import java.util.Iterator;

/**
 * Created by debasishc on 12/8/16.
 */
public class DoubleEndedQueueImplArray<E> extends QueueImplArray<E> implements DoubleEndedQueue<E> {

    public DoubleEndedQueueImplArray(int size) {
        super(size);
    }

    @Override
    public void inject(E value) {
        enqueue(value);
    }

    @Override
    public E eject() {
        if (length <= 0) {
            return null;
        }
        end = (end + array.length - 1) % array.length;
        E value = array[end];
        length--;
        return value;
    }

    @Override
    public E peekLast() {
        if (length <= 0) {
            return null;
        }
        return array[(end + array.length - 1) % array.length];
    }

    @Override
    public void push(E value) {
        if (length >= array.length) {
            throw new NoSpaceException("No more space to add an element");
        }
        start = (start + array.length - 1) % array.length;
        array[start] = value;
        length++;
    }

    @Override
    public E pop() {
        return dequeue();
    }

    public static void main(String [] args){
        DoubleEndedQueue<Integer> store = new DoubleEndedQueueImplArray<>(4);
        store.push(4);
        store.push(3);
        store.push(2);
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());
        store.inject(4);
        store.inject(3);
        store.inject(2);
        System.out.println(store.eject());
        System.out.println(store.eject());
        System.out.println(store.eject());
        System.out.println(store.eject());
        store.inject(4);
        store.inject(3);
        store.inject(2);
        System.out.println(store.eject());
        System.out.println(store.eject());
        System.out.println(store.eject());
        System.out.println(store.eject());

        store.push(4);
        store.push(3);
        store.push(2);
        System.out.println(store.eject());
        System.out.println(store.eject());
        System.out.println(store.eject());
        System.out.println(store.eject());

        store.inject(4);
        store.inject(3);
        store.inject(2);
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());


    }

    @Override
    public E checkFirst() {
        return peek();
    }

    @Override
    public void insert(E value) {
        enqueue(value);
    }

    @Override
    public E pickFirst() {
        return dequeue();
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
