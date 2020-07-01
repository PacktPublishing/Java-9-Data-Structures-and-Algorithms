package com.example.functional;

/**
 * Created by debasishc on 17/11/16.
 */
public class BinomialForest<E> {
    public static class BinomialTree<E>{
        BinomialTree<E> merge(BinomialTree<E> rhs){return null;}
    }
    public static class CombinationBinomialTree<E> extends BinomialTree<E>{
        protected BinomialTree<E> topTree;
        protected BinomialTree<E> subordinateTree;
        int height;

        @Override
        public BinomialTree<E> merge(
                BinomialTree<E> rhs) {
            return null;
        }


    }
}
