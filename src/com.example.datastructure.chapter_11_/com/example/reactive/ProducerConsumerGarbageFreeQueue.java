package com.example.reactive;

import com.example.functional.NoArgumentExpression;
import com.example.functional.TwoArgumentStatement;

import java.io.File;
import java.io.PrintStream;

/**
 * Created by debasishc on 17/1/17.
 */
public class ProducerConsumerGarbageFreeQueue<E> {
    enum EventType{
        INVOCATION, ERROR, COMPLETION
    }
    public class Event{
        E value;
        Exception error;
        EventType eventType;
    }
    ThreadSafeFixedLengthSpinlockGarbageFreeQueue<Event> queue;
    boolean alive = true;
    Thread [] threads;
    NoArgumentExpression<Event> eventCreator;
    TwoArgumentStatement<Event, Event> eventCopier;

    public ProducerConsumerGarbageFreeQueue(int bufferSize, int threadCount, Consumer<E> consumer,
                                            NoArgumentExpression<E> creator,
                                            TwoArgumentStatement<E,E> copier){
        eventCreator = () -> {
            Event evt = new Event();
            evt.value = creator.value();
            return evt;
        };
        eventCopier = (t,s)->{
            copier.doSomething(t.value,s.value);
            t.error = s.error;
            t.eventType = s.eventType;
        };
        queue = new ThreadSafeFixedLengthSpinlockGarbageFreeQueue<>(bufferSize,
                eventCreator, eventCopier);
        threads = new Thread[threadCount];
        Runnable consumerCode = ()->{
            try{
                Event e = eventCreator.value();
                while(alive || queue.currentElementCount()>0){
                    queue.dequeue(e);
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
    public Event produce(E value, Event event) throws InterruptedException {
        if(!alive){
            throw new IllegalStateException("The queue is already closed");
        }
        if(event==null) {
            event = new Event();
        }
        event.value = value;
        event.eventType = EventType.INVOCATION;
        queue.enqueue(event);
        return event;
    }
    public void markCompleted() throws InterruptedException {
        Event event = eventCreator.value();
        event.eventType = EventType.COMPLETION;
        queue.enqueue(event);
    }
    public Event sendError(Exception ex, Event event) throws InterruptedException {
        if(event==null) {
            event = new Event();
        }
        event.error = ex;
        event.eventType = EventType.ERROR;
        queue.enqueue(event);
        return event;
    }

    public void joinThreads() throws InterruptedException {
        for(Thread t: threads){
            t.join();
        }
    }

    public static void main(String [] args)
            throws Exception {
        PrintStream out = new PrintStream(new File("output"));

        class MutableInteger{
            int value;
        }

        ProducerConsumerGarbageFreeQueue<MutableInteger>
                queue = new ProducerConsumerGarbageFreeQueue<>(1024, 20, out::println,
                ()->new MutableInteger(), (t,s)->t.value = s.value);
        long start = System.currentTimeMillis();
        ProducerConsumerGarbageFreeQueue.Event event = null;
        MutableInteger mutableInteger = new MutableInteger();
        for(int i=0;i<1000000;i++){
            mutableInteger.value = i;
            event = queue.produce(mutableInteger, event);
        }
        queue.markCompleted();
        queue.joinThreads();
        out.flush();
        out.close();
        System.out.println("Time in ms: "+(System.currentTimeMillis()-start));

    }
}
