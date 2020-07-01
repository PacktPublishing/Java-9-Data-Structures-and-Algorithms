package com.example.reactive;

import java.util.concurrent.Semaphore;

/**
 * Created by debasishc on 24/1/17.
 */
public class SemaphoreExample {
    volatile int threadSafeInt = 0;
    Semaphore semaphore = new Semaphore(1);
    public int incremementAndGet() throws InterruptedException{
        semaphore.acquire();
        int previousValue = threadSafeInt++;
        semaphore.release();
        return previousValue;
    }
}
