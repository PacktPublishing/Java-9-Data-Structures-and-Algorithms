package com.example.functional;

/**
 * Created by debasishc on 29/1/17.
 */
@FunctionalInterface
public interface OneArgumentStatementWithException<E> {
    void doSomething(E input) throws Exception;
}
