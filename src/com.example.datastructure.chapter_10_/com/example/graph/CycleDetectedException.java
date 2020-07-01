package com.example.graph;

/**
 * Created by debasishc on 26/12/16.
 */
public class CycleDetectedException extends RuntimeException {
    public CycleDetectedException(String message) {
        super(message);
    }
}
