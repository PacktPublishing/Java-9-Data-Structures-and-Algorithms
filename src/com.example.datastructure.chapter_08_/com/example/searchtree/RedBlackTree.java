package com.example.searchtree;

import com.example.tree.BinaryTree;

/**
 * Created by debasishc on 2/11/16.
 */
public class RedBlackTree<E extends Comparable<E>> extends BinarySearchTree<E>{
    public static class Node<E> extends BinaryTree.Node<E>{
        protected int blackHeight = 0;
        protected boolean black = false;
        public Node(BinaryTree.Node parent,
                    BinaryTree containerTree, E value) {
            super(parent, containerTree, value);
        }
    }

    @Override
    protected  BinaryTree.Node<E> newNode(
            BinaryTree.Node<E> parent, BinaryTree<E> containerTree,
            E value) {
        return new Node(parent, containerTree, value);
    }

    private void nullSafeComputeBlackHeight(Node<E> node){
        Node<E> left = (Node<E>) node.getLeft();
        Node<E> right = (Node<E>) node.getRight();
        int leftHeight = left==null? 1 : left.blackHeight;
        int rightHeight = right==null? 1 :right.blackHeight;
        if(node.black)
            node.blackHeight =  Math.max(leftHeight, rightHeight)+1;
        else
            node.blackHeight =  Math.max(leftHeight, rightHeight);
    }

    private int nullSafeBlackHeight(Node<E> node){
        if(node==null){
            return 1;
        }else{
            return node.blackHeight;
        }
    }


//    @Override
//    protected void rotate(BinaryTree.Node<E> node, boolean left) {
//        Node<E> n = (Node<E>) node;
//        Node<E> child;
//        if(left){
//            child = (Node<E>) n.getRight();
//        }else{
//            child = (Node<E>) n.getLeft();
//        }
//        super.rotate(node, left);
//        if(node!=null){
//            nullSafeComputeBlackHeight(n);
//        }
//        if(child!=null){
//            nullSafeComputeBlackHeight(child);
//        }
//        ((Node<E>)node).leftChild
//                = node.getParent() !=null && node.getParent().getLeft() == node;
//        ((Node<E>)child).leftChild
//                = child.getParent() !=null && child.getParent().getLeft() == child;
//    }

    protected void rebalanceForInsert(Node<E> node){
        if(node.getParent() == null){
            node.black = true;
        }else{
            Node<E> parent = (Node<E>) node.getParent();
            if(parent.black){
                return;
            }else{
                Node<E> grandParent = (Node<E>) parent.getParent();
                boolean nodeLeftGrandChild = grandParent.getLeft()== parent;

                Node<E> uncle
                        = nodeLeftGrandChild?
                        (Node<E>) grandParent.getRight(): (Node<E>) grandParent.getLeft();
                if(!nullSafeBlack(uncle)){
                    if(grandParent!=root)
                        grandParent.black = false;
                    uncle.black = true;
                    parent.black = true;
                    rebalanceForInsert(grandParent);
                }else{
                    boolean middleChild
                            = nodeLeftGrandChild?
                            parent.getRight() == node:parent.getLeft() == node;
                    if (middleChild){
                        rotate(parent, nodeLeftGrandChild);
                        node = parent;
                        parent = (Node<E>) node.getParent();
                    }
                    parent.black = true;
                    grandParent.black = false;
                    rotate(grandParent, !nodeLeftGrandChild);
                }
            }

        }
    }

    protected void rebalanceForDelete(Node<E> parent, boolean nodeDirectionLeft){
        if(parent==null){
            return;
        }
        Node<E> node =
                (Node<E>) (nodeDirectionLeft? parent.getLeft(): parent.getRight());
        if(!nullSafeBlack(node)){
            node.black = true;
            return;
        }

        Node<E> sibling =
                (Node<E>) (nodeDirectionLeft? parent.getRight(): parent.getLeft());


        Node<E> nearNephew =
                (Node<E>) (nodeDirectionLeft?sibling.getLeft():sibling.getRight());

        Node<E> awayNephew =
                (Node<E>) (nodeDirectionLeft?sibling.getRight():sibling.getLeft());

        if(parent.black){
            if(sibling.black){
                if(nullSafeBlack(nearNephew) && nullSafeBlack(awayNephew)){
                    sibling.black = false;
                    if(parent.getParent()!=null){
                        rebalanceForDelete (
                                (Node<E>) parent.getParent(), parent.getParent().getLeft() == parent);
                    }
                }else if(!nullSafeBlack(awayNephew)){
                    awayNephew.black = true;
                    rotate(parent, nodeDirectionLeft);
                }else{
                    nearNephew.black = true;
                    rotate(sibling, !nodeDirectionLeft);
                    rotate(parent, nodeDirectionLeft);
                }

            }else{
                parent.black = false;
                sibling.black = true;
                rotate(parent, nodeDirectionLeft);
                rebalanceForDelete(parent, nodeDirectionLeft);
            }
        }else{

            if(nullSafeBlack(nearNephew)){
                rotate(parent, nodeDirectionLeft);
            }else{
                parent.black = true;
                rotate(sibling, !nodeDirectionLeft);
                rotate(parent, nodeDirectionLeft);
            }
        }

    }

    protected boolean nullSafeBlack(Node<E> node){
        if(node == null){
            return true;
        }else{
            return node.black;
        }
    }


    protected void displayText(BinaryTree.Node<E> parent, int depth){

        for(int i=0;i<depth;i++){
            System.out.print("    ");
        }
        if(parent==null){
            System.out.println("*");
            return;
        }
        if(((Node<E>)parent).black){
            System.out.print("B");
        }else{
            System.out.print("R");
        }
        System.out.println(parent.getValue());
        displayText(parent.getLeft(),depth+1);
        displayText(parent.getRight(),depth+1);
    }

    protected void checkSanity(Node<E> node){
        if(node==null){
            return;
        }
        if(root!=null && !((Node<E>)root).black)
            throw new IllegalStateException("Root not black");
        checkSanity((Node<E>) node.getLeft());
        checkSanity((Node<E>) node.getRight());
        nullSafeComputeBlackHeight(node);
        int leftHeight = nullSafeBlackHeight((Node<E>) node.getLeft());
        int rightHeight = nullSafeBlackHeight(
                (Node<E>) node.getRight());
        if(leftHeight != rightHeight){
            throw new IllegalStateException("Black height does not match");
        }else if(!node.black){
            if(!nullSafeBlack((Node<E>) node.getLeft())
                    || !nullSafeBlack((Node<E>) node.getLeft())){
                displayText(node, 0);
                throw new IllegalStateException("Double red");
            }
        }
    }

    @Override
    public BinaryTree.Node<E> insertValue(E value) {
        Node<E> node = (Node<E>) super.insertValue(value);
        if(node!=null)
            rebalanceForInsert(node);
        return node;
    }

    @Override
    public BinaryTree.Node<E> deleteValue(E value) {
        Node<E> node = (Node<E>) super.deleteValue(value);

        if(node !=null && node.black && node.getParent()!=null){
            Node<E> parentsCurrentChild =
                    (Node<E>) (node.getLeft() == null ? node.getRight(): node.getLeft());
            if(parentsCurrentChild!=null){
                boolean isLeftChild
                        = parentsCurrentChild.getParent().getLeft() == parentsCurrentChild;
                rebalanceForDelete(
                        (Node<E>) node.getParent(), isLeftChild);
            }else{
                boolean isLeftChild
                        = node.getParent().getRight()!=null;
                rebalanceForDelete(
                        (Node<E>) node.getParent(), isLeftChild);
            }

        }
        return node;
    }

    public static void main(String [] args) {
        RedBlackTree<Integer> tree = new RedBlackTree<>();
        for (int i = 0; i < 20; i++) {
            tree.insertValue((int)(Math.random()*1000000));
        }
//        //tree.displayText();
        for(int i=0;i<2000000;i++){
            tree.insertValue((int)(Math.random()*1000000));
        }
        //tree.displayText();
        tree.checkSanity((Node<Integer>) tree.getRoot());

        for (int i = 1; i < 200; i++) {
            int value = (int)(Math.random()*1000000);
            tree.deleteValue(value);
            System.out.println(value);
            tree.checkSanity((Node<Integer>) tree.getRoot());
        }

    }
}
