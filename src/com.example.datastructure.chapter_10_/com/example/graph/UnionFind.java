package com.example.graph;

import com.example.searchtree.BinarySearchTree;
import com.example.searchtree.RedBlackTree;
import com.example.tree.BinaryTree;

/**
 * Created by debasishc on 27/12/16.
 */
public class UnionFind<E extends Comparable<E>> {
    private class Node implements Comparable<Node>{
        Node parent;
        E object;
        int rank;

        public Node(E  object) {
            this.object = object;
            rank = 0;
        }


        @Override
        public int compareTo(Node o) {
            return object.compareTo(o.object);
        }
    }

    BinarySearchTree<Node> allNodes = new RedBlackTree<>();
    int partitionCount;

    public void add(E object){
        Node n = new Node(object);
        allNodes.insertValue(n);
        partitionCount++;
    }

    Node findRoot(Node n){
        if(n.parent==null){
            return n;
        }else{
            return findRoot(n.parent);
        }
    }

    public void union(E o1, E o2){
        BinaryTree.Node<Node> node1 = allNodes.searchValue(new Node(o1));
        BinaryTree.Node<Node> node2 = allNodes.searchValue(new Node(o2));
        if(node1==null || node2==null){
            throw new IllegalArgumentException("Objects not found");
        }
        Node n1 = node1.getValue();
        Node n2 = node2.getValue();
        Node p1 = findRoot(n1);
        Node p2 = findRoot(n2);
        if(p1==p2){
            return;
        }
        int r1 = n1.rank;
        int r2 = n2.rank;
        if(r1>r2){
            p2.parent = p1;
        }else if(r2>r1){
            p1.parent = p2;
        }else{
            p2.parent = p1;
            p1.rank++;
        }
        partitionCount--;

    }

    public E find(E object){
        BinaryTree.Node<Node> node1 = allNodes.searchValue(new Node(object));
        if(node1==null){
            throw new IllegalArgumentException("Objects not found");
        }
        Node n = node1.getValue();
        return findRoot(n).object;
    }

    public int getPartitionCount() {
        return partitionCount;
    }

    public static void main(String [] args){
        UnionFind<Integer> unionFind = new UnionFind<>();

        for(int i=0;i<10;i++){
            unionFind.add(i);
        }

        unionFind.union(0,2);
        unionFind.union(0,3);
        unionFind.union(4,3);
        unionFind.union(9,0);

        for(int i=0;i<10;i++){
            System.out.println(i+ " -> "+ unionFind.find(i));
        }
    }
}
