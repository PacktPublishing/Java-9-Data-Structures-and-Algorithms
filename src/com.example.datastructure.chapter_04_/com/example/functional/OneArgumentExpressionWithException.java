package com.example.functional;

/**
 * Created by debasishc on 11/9/16.
 */
@FunctionalInterface
public interface OneArgumentExpressionWithException<A,R> {
    R compute(A a) throws Exception;
}
