package com.example.adt;

/**
 * Created by debasishc on 12/8/16.
 */
public class NoSpaceException extends RuntimeException{
    public NoSpaceException(String message) {
        super(message);
    }
}
