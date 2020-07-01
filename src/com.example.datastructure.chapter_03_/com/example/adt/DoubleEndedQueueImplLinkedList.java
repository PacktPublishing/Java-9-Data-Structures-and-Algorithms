package com.example.adt;

import com.example.datastructure.DoublyLinkedList;
import com.example.datastructure.LinkedList;

/**
 * Created by debasishc on 12/8/16.
 */
public class DoubleEndedQueueImplLinkedList<E> extends StackImplLinkedList<E> implements DoubleEndedQueue<E> {

    @Override
    protected LinkedList<E> getNewLinkedList() {
        return new DoublyLinkedList<E>();
    }

    @Override
    public void inject(E value) {
        list.appendLast(value);
    }

    @Override
    public E eject() {
        if(list.getLength()==0){
            return null;
        }
        E value = list.getLast();
        list.removeLast();
        return value;
    }

    @Override
    public E peekLast() {
        if(list.getLength()==0){
            return null;
        }
        return list.getLast();
    }

    public static void main(String [] args){
        DoubleEndedQueue<Integer> store = new DoubleEndedQueueImplLinkedList<>();
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
}
