package com.example.graph;

import com.example.datastructure.LinkedList;
import com.example.functional.OneArgumentExpression;
import com.example.functional.ThreeArgumentStatement;
import com.example.functional.TwoArgumentStatement;
import com.example.searchtree.BinarySearchTree;
import com.example.searchtree.RedBlackTree;
import com.example.tree.BinaryTree;
import com.example.adt.*;
import com.example.priorityqueue.*;


/**
 * Created by debasishc on 19/12/16.
 */
public interface Graph<V, E> {
    int addVertex();
    void removeVertex(int id);
    void addEdge(int source, int target);
    void removeEdge(int source, int target);
    boolean isAdjacent(int source, int target);
    LinkedList<Integer> getNeighbors(int source);
    void setVertexValue(int vertex, V value);
    V getVertexValue(int vertex);
    void setEdgeValue(int source, int target, E value);
    E getEdgeValue(int source, int target);
    boolean isUndirected();
    BinarySearchTree<Integer> getAllVertices();
    int maxVertexID();
    enum TraversalType{
        DFT, BFT
    }
    class Edge implements Comparable<Edge>{
        int source;
        int target;

        public Edge(int source, int target) {
            this.source = source;
            this.target = target;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass())
                return false;

            Edge edge = (Edge) o;

            if (source != edge.source) return false;
            return target == edge.target;

        }

        @Override
        public int hashCode() {
            int result = source;
            result = 31 * result + target;
            return result;
        }


        @Override
        public int compareTo(Edge o) {
            if(source!=o.source){
                return source - o.source;
            }else {
                return target - o.target;
            }
        }
    }

    default void visitAllConnectedEdges(int startingNode, ThreeArgumentStatement<Integer, Integer, E> visitor,
                                        TraversalType type){

        OrderedStore<Edge> toBeProcessed = null;
        boolean doneProcessing[] = new boolean[maxVertexID()+1];
        switch (type){
            case BFT: toBeProcessed = new QueueImplLinkedList<Edge>(); break;
            case DFT: toBeProcessed = new StackImplLinkedList<Edge>(); break;
        }
        toBeProcessed.insert(new Edge(-1, startingNode));
        while (toBeProcessed.checkFirst()!=null){
            Edge edge = toBeProcessed.pickFirst();
            LinkedList<Integer> neighbors = getNeighbors(edge.target);
            if(edge.source>=0) {
                visitor.doSomething(edge.source, edge.target, getEdgeValue(edge.source, edge.target));
            }
            if(doneProcessing[edge.target]){
                continue;
            }

            for(int target: neighbors){
                if(isUndirected() && doneProcessing[target]){
                    continue;
                }
                Edge nextEdge = new Edge(edge.target, target);
                if(nextEdge.target!=edge.source)
                    toBeProcessed.insert(nextEdge);
            }

            doneProcessing[edge.target] = true;
        }
    }

    default void visitAllConnectedVertices(int startingNode, TwoArgumentStatement<Integer,  V> visitor,
                                           TraversalType type) {
        OrderedStore<Integer> toBeProcessed = null;
        boolean doneProcessing[] = new boolean[maxVertexID()+1];
        switch (type){
            case BFT: toBeProcessed = new QueueImplLinkedList<>(); break;
            case DFT: toBeProcessed = new StackImplLinkedList<>(); break;
        }
        toBeProcessed.insert(startingNode);
        while(toBeProcessed.checkFirst()!=null){
            int currentVertex = toBeProcessed.pickFirst();
            if(doneProcessing[currentVertex]){
                continue;
            }
            doneProcessing[currentVertex] = true;
            visitor.doSomething(currentVertex, getVertexValue(currentVertex));
            for(int neighbor:getNeighbors(currentVertex)){
                if(doneProcessing[neighbor]==false){
                    toBeProcessed.insert(neighbor);
                }
            }
        }
    }

    default boolean detectCycle(){
        BinarySearchTree<Integer> allVertices = getAllVertices();

        try {
            if (isUndirected()) {
                while (allVertices.getRoot() != null) {
                    int start = allVertices.getRoot().getValue();
                    RedBlackTree<Integer> metAlready = new RedBlackTree<>();
                    metAlready.insertValue(start);
                    allVertices.deleteValue(start);
                    visitAllConnectedEdges(start,
                            (s, t, v) -> {
                                if(metAlready.searchValue(t) ==
                                        null) {
                                    metAlready.insertValue(t);
                                    allVertices.deleteValue(t);
                                }else if(metAlready.searchValue(s) == null){
                                    metAlready.insertValue(s);
                                    allVertices.deleteValue(s);
                                }else{
                                    throw new CycleDetectedException("found "+t);
                                }
                            }, TraversalType.DFT);
                }
            } else {
                while (allVertices.getRoot() != null) {
                    checkDirectedCycleFromVertex(
                            com.example.functional.LinkedList.
                                    <Integer>emptyList().add(allVertices.getRoot().getValue()),
                            allVertices);
                }
            }
        }catch (CycleDetectedException ex){
            return true;
        }
        return false;
    }
    default void checkDirectedCycleFromVertex(
            com.example.functional.LinkedList<Integer> path, BinarySearchTree<Integer> allVertices){
        int top = path.head();
        allVertices.deleteValue(top);
        LinkedList<Integer> neighbors = getNeighbors(top);
        for(int n:neighbors){
            com.example.functional.LinkedList<Integer> pathPart = path;
            while (!pathPart.isEmpty()){
                int head = pathPart.head();
                if(head == n){
                    throw new CycleDetectedException("");
                }
                pathPart = pathPart.tail();
            }
            checkDirectedCycleFromVertex(path.add(n), allVertices);
        }
    }



    default LinkedList<Edge> spanningTree(int startingNode){
        if(!isUndirected()){
            throw new IllegalStateException("Spanning tree only applicable to undirected trees");
        }

        LinkedList<Edge> subGraph = new LinkedList<>();
        OrderedStore<Edge> toBeProcessed = new StackImplLinkedList<>();
        RedBlackTree<Integer> doneProcessing = new RedBlackTree<>();

        toBeProcessed.insert(new Edge(-1, startingNode));
        while(toBeProcessed.checkFirst()!=null){
            Edge edge = toBeProcessed.pickFirst();
            int currentVertex = edge.target;
            if(doneProcessing.searchValue(currentVertex)!=null){
                continue;
            }
            doneProcessing.insertValue(currentVertex);
            if(edge.source>=0)
                subGraph.appendLast(edge);
            getNeighbors(currentVertex).forEach((t)->toBeProcessed.insert(new Edge(currentVertex, t)));
        }
        return subGraph;
    }

    class CostEdge extends Edge{
        Integer cost;

        public CostEdge(int source, int target, int cost) {
            super(source, target);
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge o) {
            return cost - ((CostEdge)o).cost;
        }
    }

    default LinkedList<Edge> minimumSpanningTree(OneArgumentExpression<E,Integer> costFinder){
        if(!isUndirected()){
            throw new IllegalStateException("Spanning tree only applicable to undirected trees");
        }
        LinkedList<Edge> subGraph = new LinkedList<>();
        PriorityQueue<CostEdge> edgeQueue = new LinkedHeap<>((x, y)->x.compareTo(y));
        UnionFind<Integer> unionFind = new UnionFind<>();
        this.visitAllConnectedEdges(getAllVertices().getRoot().getValue(),
                (s,t,v)-> edgeQueue.enqueue(new CostEdge(s,t,costFinder.compute(v))), TraversalType.DFT);
        this.getAllVertices().traverseDepthFirstNonRecursive((x)-> unionFind
                        .add(x),
                BinaryTree.DepthFirstTraversalType.PREORDER);
        while(unionFind.getPartitionCount()>1 && edgeQueue.checkMinimum()!=null){
            Edge e = edgeQueue.dequeueMinimum();
            int sGroup = unionFind.find(e.source);
            int tGroup = unionFind.find(e.target);
            if(sGroup!=tGroup){
                subGraph.appendLast(e);
                unionFind.union(e.source, e.target);
            }
        }
        return subGraph;

    }
}
