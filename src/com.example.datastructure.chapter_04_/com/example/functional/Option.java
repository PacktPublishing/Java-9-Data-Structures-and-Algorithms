package com.example.functional;

/**
 * Created by debasishc on 28/8/16.
 */
public abstract class Option<E> {
    public static class None<E> extends Option<E>{

        @Override
        public <R> Option<R> flatMap(OneArgumentExpression<E, Option<R>> transformer) {
            return new None<>();
        }

        @Override
        public E get() {
            throw new NoValueException("get() invoked on None");
        }

        @Override
        public <R> Option<R> map(OneArgumentExpression<E, R> transformer) {
            return new None<>();
        }

        @Override
        public void forEach(OneArgumentStatement<E> statement) {
        }
    }
    public static class Some<E> extends Option<E>{
        E value;
        public Some(E value){
            this.value = value;
        }
        public E get(){
            return value;
        }
        public <R> Option<R> map(OneArgumentExpression<E,R> transformer){
            return Option.optionOf(transformer.compute(value));
        }
        public <R> Option<R> flatMap(OneArgumentExpression<E,Option<R>> transformer){
            return transformer.compute(value);
        }
        public void forEach(OneArgumentStatement<E> statement){
            statement.doSomething(value);
        }
    }

    public static <X> Option<X>  optionOf(X value){
        if(value == null){
            return new None<>();
        }else{
            return new Some<>(value);
        }
    }

    public abstract E get();
    public abstract <R> Option<R> map(OneArgumentExpression<E,R> transformer);
    public abstract <R> Option<R> flatMap(OneArgumentExpression<E,Option<R>> transformer);
    public abstract void forEach(OneArgumentStatement<E> statement);
}
