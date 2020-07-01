package com.example.reactive;

/**
 * Created by debasishc on 13/1/17.
 */
public interface Consumer<E> {
    void onMessage(E message);
    default void onError(Exception error){
        error.printStackTrace();
    }
    default void onComplete(){

    }
}
