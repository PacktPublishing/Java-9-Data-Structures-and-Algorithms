package com.example.functional;

/**
 * Created by debasishc on 1/9/16.
 */
@FunctionalInterface
public interface TwoArgumentExpression<A,B,R> {
    R compute(A lhs, B rhs);
}
