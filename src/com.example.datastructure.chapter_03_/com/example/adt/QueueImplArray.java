package com.example.adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by debasishc on 12/8/16.
 */
public class QueueImplArray<E>  implements Queue<E>{
    protected E[] array;
    protected int start=0;
    protected int end=0;
    protected int length=0;
    public QueueImplArray(int size){
        array = (E[]) new Object[size];
    }

    @Override
    public void enqueue(E value) {
        if(length>=array.length){
            throw new NoSpaceException("No more space to add an element");
        }
        array[end] = value;
        end = (end+1) % array.length;
        length++;

    }

    @Override
    public E dequeue() {
        if(length<=0){
            return null;
        }
        E value = array[start];
        start = (start+1) % array.length;
        length--;
        return value;
    }

    @Override
    public E peek() {
        if(length<=0){
            return null;
        }
        return array[start];
    }

    public static void main(String [] args){
        Queue<Integer> store = new QueueImplArray<>(4);
        store.enqueue(4);
        store.enqueue(3);
        store.enqueue(2);
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        store.enqueue(4);
        store.enqueue(3);
        store.enqueue(2);
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        store.enqueue(4);
        store.enqueue(3);
        store.enqueue(2);
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());
        System.out.println(store.dequeue());

    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int nextPosition = start;
            @Override
            public boolean hasNext() {
                if(length==0){
                    return false;
                }
                if(start<end) {
                    return nextPosition >= start &&
                            nextPosition < end;
                }else{
                    return nextPosition>start || nextPosition<=end;
                }
            }

            @Override
            public E next() {
                int pos = nextPosition;
                nextPosition = (nextPosition + 1) % array.length;
                return array[pos];
            }
        };
    }
}
