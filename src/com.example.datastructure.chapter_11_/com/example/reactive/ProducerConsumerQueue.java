package com.example.reactive;

import java.io.File;
import java.io.PrintStream;

/**
 * Created by debasishc on 17/1/17.
 */
public class ProducerConsumerQueue<E> {
    enum EventType{
        INVOCATION, ERROR, COMPLETION
    }
    class Event{
        E value;
        Exception error;
        EventType eventType;
    }
    ThreadSafeFixedLengthSpinlockQueue<Event> queue;
    boolean alive = true;
    Thread [] threads;

    public ProducerConsumerQueue(int bufferSize, int threadCount, Consumer<E> consumer){
        queue = new ThreadSafeFixedLengthSpinlockQueue<>(bufferSize);
        threads = new Thread[threadCount];
        Runnable consumerCode = ()->{
            try{
                while(alive || queue.currentElementCount()>0){
                    Event e = queue.dequeue();
                    switch (e.eventType) {
                        case INVOCATION:
                            consumer.onMessage(e.value);
                            break;
                        case ERROR:
                            consumer.onError(e.error);
                            break;
                        case COMPLETION:
                            alive = false;
                            consumer.onComplete();
                    }
                }

                queue.killDequeuers();

            } catch (InterruptedException e) {

            } finally{

            }
        };
        for(int i=0;i<threadCount;i++) {
            threads[i] = new Thread(consumerCode);
            threads[i].start();
        }
    }
    public void produce(E value) throws InterruptedException {
        Event event = new Event();
        event.value = value;
        event.eventType = EventType.INVOCATION;
        queue.enqueue(event);
    }
    public void produceExternal(E value) throws InterruptedException {
        Event event = new Event();
        event.value = value;
        event.eventType = EventType.INVOCATION;
        queue.enqueueProducerOnly(event);
    }
    public void markCompleted() throws InterruptedException {
        Event event = new Event();
        event.eventType = EventType.COMPLETION;
        queue.enqueue(event);
    }
    public void sendError(Exception ex) {
        Event event = new Event();
        event.error = ex;
        event.eventType = EventType.ERROR;
        try {
            queue.enqueue(event);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void joinThreads() throws InterruptedException {
        for(Thread t: threads){
            t.join();
        }
    }

    public static void main(String [] args)
            throws Exception {
        PrintStream out = new PrintStream(new File("output"));
        ProducerConsumerQueue<Integer> queue = new ProducerConsumerQueue<>(1024, 20, out::println);
        long start = System.currentTimeMillis();
        for(int i=0;i<5_00_000;i++){
            queue.produce(i);
        }
        queue.markCompleted();
        queue.joinThreads();
        out.flush();
        out.close();
        System.out.println("Time in ms: "+(System.currentTimeMillis()-start));

    }
}
