package com.example.reactive;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by debasishc on 13/1/17.
 */
public class ThreadSafeFixedLengthBlockingQueue<E> {
    Semaphore underflowSemaphore;
    Semaphore overflowSemaphore;
    AtomicInteger nextEnqueueIndex;
    AtomicInteger nextDequeueIndex;
    E[] store;
    Semaphore [] enqueueLocks;
    Semaphore [] dequeueLocks;
    int length;
    boolean alive = true;
    public ThreadSafeFixedLengthBlockingQueue(int length){
        this.length = length;
        store = (E[]) new Object[length];
        nextEnqueueIndex = new AtomicInteger();
        nextDequeueIndex = new AtomicInteger();
        underflowSemaphore = new Semaphore(length);
        overflowSemaphore = new Semaphore(length);
        underflowSemaphore.acquireUninterruptibly(length);
        enqueueLocks = new Semaphore[length];
        dequeueLocks = new Semaphore[length];
        for(int i=0;i<length;i++){
            enqueueLocks[i] = new Semaphore(1);
            dequeueLocks[i] = new Semaphore(1);
            dequeueLocks[i].acquireUninterruptibly();
        }
    }


    public void enqueue(E value) throws InterruptedException {
        overflowSemaphore.acquire();
        int index = (length + nextEnqueueIndex.getAndIncrement() % length) % length;
        enqueueLocks[index].acquire();
        store[index] = value;
        dequeueLocks[index].release();
        underflowSemaphore.release();
    }

    public E dequeue() throws InterruptedException {
        while (alive && !underflowSemaphore.tryAcquire(1, TimeUnit.SECONDS));
        if(!alive){
            Thread.currentThread().interrupt();
        }
        int index = (length + nextDequeueIndex.getAndIncrement() % length) % length;
        dequeueLocks[index].acquire();
        E value = store[index];
        enqueueLocks[index].release();
        overflowSemaphore.release();
        return value;
    }

    public int currentElementCount(){
        return underflowSemaphore.availablePermits();
    }

    public void killDequeuers(){
        alive = false;
    }


    public static void main(String [] args)
            throws Exception {
        final ThreadSafeFixedLengthBlockingQueue<Integer>
                queue = new ThreadSafeFixedLengthBlockingQueue<>(4096);
        PrintStream out = new PrintStream(new File("output"));
        long start = System.currentTimeMillis();
        Runnable dequeer = ()->{
            while(true) {
                int value = 0;
                try {
                    value = queue.dequeue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.println(Thread.currentThread().getId() +
                        "  " + (System.currentTimeMillis()-start) +" " + value);

            }
        };
        Runnable dequeer2 = ()->{
            while(true) {
                int value = 0;
                try {
                    value = queue.dequeue();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                out.println(Thread.currentThread().getId() +
                        "  " + (System.currentTimeMillis()-start) +" " + value);
            }
        };
        Runnable enqueer = ()->{
            for(int i=0;i<1000000;i++) {
                try {
                    queue.enqueue(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for(int i=0;i<2;i++){
            new Thread(enqueer).start();
        }
        for(int i=0;i<10;i++){
            new Thread(dequeer).start();
        }
        //new Thread(dequeer2).start();
    }
}
