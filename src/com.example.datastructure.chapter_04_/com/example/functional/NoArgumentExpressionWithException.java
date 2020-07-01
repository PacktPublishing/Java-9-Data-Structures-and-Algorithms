package com.example.functional;

/**
 * Created by debasishc on 8/9/16.
 */
@FunctionalInterface
public interface NoArgumentExpressionWithException<R> {
    R evaluate() throws Exception;
}
