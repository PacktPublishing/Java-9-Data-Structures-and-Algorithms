package com.example.functional;

/**
 * Created by debasishc on 16/2/17.
 */
@FunctionalInterface
public interface FunctionalInterfaceWithDefaultMethod {
    int modify(int x);
    default int modifyTwice(int x){return modify(modify(x));}
}
