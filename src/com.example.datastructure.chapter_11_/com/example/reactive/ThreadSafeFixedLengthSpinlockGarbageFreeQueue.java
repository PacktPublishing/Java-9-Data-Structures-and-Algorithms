package com.example.reactive;

import com.example.functional.NoArgumentExpression;
import com.example.functional.TwoArgumentStatement;

import java.io.File;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by debasishc on 13/1/17.
 */
public class ThreadSafeFixedLengthSpinlockGarbageFreeQueue<E> {
    volatile int nextEnqueueIndex;
    volatile int nextDequeueIndex;
    E[] store;
    AtomicBoolean[] enqueueLocks;
    AtomicBoolean[] dequeueLocks;
    AtomicInteger currentElementCount = new AtomicInteger(0);
    int length;
    volatile boolean alive = true;
    TwoArgumentStatement<E,E> copier;
    public ThreadSafeFixedLengthSpinlockGarbageFreeQueue(int length,
                                                         NoArgumentExpression<E> creator,
                                                         TwoArgumentStatement<E,E> copier){
        this.length = length;
        this.copier = copier;
        store = (E[]) new Object[length];
        enqueueLocks = new AtomicBoolean[length];
        dequeueLocks = new AtomicBoolean[length];
        for(int i=0;i<length;i++){
            store[i] = creator.value();
            enqueueLocks[i] = new AtomicBoolean(false);
            dequeueLocks[i] = new AtomicBoolean(true);
        }
    }


    public void enqueue(E value) throws InterruptedException {

        while (true) {
            int index = nextEnqueueIndex;
            nextEnqueueIndex = (nextEnqueueIndex+1) % length;
            if(enqueueLocks[index].compareAndSet(false,true)){
                currentElementCount.incrementAndGet();
                copier.doSomething(store[index], value);
                dequeueLocks[index].set(false);
                return;
            }
        }
    }

    public void dequeue(E target) throws InterruptedException {
        while(alive) {
            int index = nextDequeueIndex;
            nextDequeueIndex = (nextDequeueIndex+1) % length;
            if(dequeueLocks[index].compareAndSet(false,true)){
                currentElementCount.decrementAndGet();
                copier.doSomething(target,store[index]);
                enqueueLocks[index].set(false);
                return;
            }
        }
        throw new InterruptedException("");
    }

    public int currentElementCount(){
        return currentElementCount.get();
    }

    public void killDequeuers(){
        alive = false;
    }


    public static void main(String [] args)
            throws Exception {
//        final ThreadSafeFixedLengthSpinlockGarbageFreeQueue<Integer>
//                queue = new ThreadSafeFixedLengthSpinlockGarbageFreeQueue<>(4096);
//        PrintStream out = new PrintStream(new File("output"));
//        long start = System.currentTimeMillis();
//        Runnable dequeer = ()->{
//            while(true) {
//                int value = 0;
//                try {
//                    value = queue.dequeue();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                out.println(Thread.currentThread().getId() +
//                        "  " + (System.currentTimeMillis()-start) +" " + value);
//
//            }
//        };
//        Runnable dequeer2 = ()->{
//            while(true) {
//                int value = 0;
//                try {
//                    value = queue.dequeue();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                out.println(Thread.currentThread().getId() +
//                        "  " + (System.currentTimeMillis()-start) +" " + value);
//            }
//        };
//        Runnable enqueer = ()->{
//            for(int i=0;i<1000000;i++) {
//                try {
//                    queue.enqueue(i);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        for(int i=0;i<2;i++){
//            new Thread(enqueer).start();
//        }
//        for(int i=0;i<10;i++){
//            new Thread(dequeer).start();
//        }
//        //new Thread(dequeer2).start();
    }
}
