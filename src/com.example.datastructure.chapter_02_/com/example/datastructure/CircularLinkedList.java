package com.example.datastructure;

import java.util.Iterator;

/**
 * Created by debasishc on 10/8/16.
 */
public class CircularLinkedList<E> extends LinkedList<E>{
    @Override
    public Node<E> appendFirst(E value) {
        Node<E> newNode = super.appendFirst(value);
        last.next = first;
        return newNode;
    }

    @Override
    public Node<E> appendLast(E value) {
        Node<E> newNode =  super.appendLast(value);
        last.next = first;
        return newNode;
    }

    @Override
    public Node<E> removeFirst() {
        Node<E> newNode =  super.removeFirst();
        last.next = first;
        return newNode;
    }

    public void rotate(){
        last = first;
        first = first.next;
    }

    public static void main(String [] args){
        CircularLinkedList<Integer> linkedList = new CircularLinkedList<>();
        linkedList.appendFirst(4);
        linkedList.appendFirst(1);
        linkedList.appendFirst(2);
        linkedList.appendFirst(3);
        linkedList.appendLast(8);
        linkedList.appendLast(7);
        linkedList.appendLast(18);

        Iterator<Integer> iterator = linkedList.iterator();
        for(int i=0;i<30;i++){
            System.out.print(" "+ iterator.next());
        }

        System.out.println();

        for(int i=0;i<30;i++){
            System.out.print(" "+ linkedList.first);
            linkedList.rotate();
        }
    }

}
