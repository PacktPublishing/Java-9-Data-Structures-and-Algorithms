package com.example.tree;

import com.example.functional.LinkedList;
import com.example.functional.OneArgumentStatement;

/**
 * Created by debasishc on 16/10/16.
 */
public class FunctionalTree<E> {
    private E value;
    private LinkedList<FunctionalTree<E>> children;

    public FunctionalTree(E value,  LinkedList<FunctionalTree<E>> children) {
        this.children = children;
        this.value = value;
    }

    public  LinkedList<FunctionalTree<E>> getChildren() {
        return children;
    }

    public E getValue() {
        return value;
    }

    public void traverseDepthFirst(OneArgumentStatement<E> processor){
        processor.doSomething(value);
        children.forEach((n)-> n.traverseDepthFirst(processor));
    }

    public static void main(String [] args){
        LinkedList<FunctionalTree<Integer>> emptyList = LinkedList.emptyList();

        FunctionalTree<Integer> t1 = new FunctionalTree<>(5, emptyList);
        FunctionalTree<Integer> t2 = new FunctionalTree<>(9, emptyList);
        FunctionalTree<Integer> t3 = new FunctionalTree<>(6, emptyList);

        FunctionalTree<Integer> t4 = new FunctionalTree<>(2, emptyList);
        FunctionalTree<Integer> t5 = new FunctionalTree<>(5, emptyList.add(t1));
        FunctionalTree<Integer> t6 = new FunctionalTree<>(9, emptyList.add(t3).add(t2));
        FunctionalTree<Integer> t7 = new FunctionalTree<>(6, emptyList);
        FunctionalTree<Integer> t8 = new FunctionalTree<>(2, emptyList);

        FunctionalTree<Integer> t9 = new FunctionalTree<>(5, emptyList.add(t6).add(t5).add(t4));
        FunctionalTree<Integer> t10 = new FunctionalTree<>(1, emptyList.add(t8).add(t7));

        FunctionalTree<Integer> tree = new FunctionalTree<>(1, emptyList.add(t10).add(t9));

        tree.traverseDepthFirst(System.out::print);

    }


}
