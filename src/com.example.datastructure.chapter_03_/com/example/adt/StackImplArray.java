package com.example.adt;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by debasishc on 12/8/16.
 */
public class StackImplArray<E> implements Stack<E> {
    protected E[] array;
    int top=-1;

    public StackImplArray(int size){
        array = (E[])new Object[size];
    }
    @Override
    public void push(E value) {
        if(top == array.length-1){
            throw new NoSpaceException("No more space in stack");
        }
        top++;
        array[top] = value;
    }

    @Override
    public E pop() {
        if(top==-1){
            return null;
        }
        top--;
        return array[top+1];
    }

    @Override
    public E peek() {
        if(top==-1){
            return null;
        }
        return array[top];
    }

    public static void main(String [] args){
        Stack<Integer> store = new StackImplArray<>(4);
        store.push(4);
        store.push(3);
        store.push(2);
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());
        System.out.println(store.pop());

    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            int nextPosition = 0;
            @Override
            public boolean hasNext() {
                return nextPosition<=top;
            }

            @Override
            public E next() {
                return array[nextPosition++];
            }
        };
    }
}
