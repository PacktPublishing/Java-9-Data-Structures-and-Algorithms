package com.example.reactive;

import com.example.functional.OneArgumentExpressionWithException;
import com.example.functional.OneArgumentStatementWithException;

/**
 * Created by debasishc on 29/1/17.
 */
public abstract class EventStream<E> {
    EventStream previous;
    OneArgumentExpressionWithException mapper;
    OneArgumentExpressionWithException filter;
    public <R> EventStream<R> map(OneArgumentExpressionWithException<E,R> mapper){
        EventStream<R> mapped = new EventStream<R>() {

            @Override
            public R read() {
                return null;
            }
        };
        mapped.mapper = mapper;
        mapped.previous = this;
        return mapped;
    }
    public EventStream<E> filter(OneArgumentExpressionWithException<E,Boolean> filter){
        EventStream<E> mapped = new EventStream<E>() {

            @Override
            public E read() {
                return null;
            }
        };
        mapped.filter = filter;
        mapped.previous = this;
        return mapped;
    }
    public EventConsumer<E> consume(OneArgumentStatementWithException<E> consumer){
        EventConsumer eventConsumer = new EventConsumer(consumer, this) {
        };
        return eventConsumer;
    }
    public abstract E read();

    public static void main(String [] args){
        EventStream<Long> stream = new EventStream<Long>() {
            Long next = 1l;
            @Override
            public Long read() {
                Long ret = next++;
                if(ret<=5_000_000L){
                    return ret;
                }
                return null;
            }
        };
        stream.map((x)->x/2).filter((x)->x>1)
                .filter(EventStream::isPerfect)
                .map((x)->x)
                .consume((x)->{System.out.println(x);})
                .onError((x)->System.out.println(x))
                .process(4096,1,3);

    }
    public static boolean isPrime(long x){
        long div = 2;
        while(true){
            if(x%div==0){
                return false;
            }
            long quotient = x/div;
            if(quotient<div){
                return true;
            }
            div++;
        }
    }
    public static boolean isPerfect(long x){
        long div = 2;
        long sum=0;
        while(true){
            long quotient = x/div;
            if(quotient<div){
                break;
            }
            if(x%div==0){
                sum+=div;
                if(quotient!=div){
                    sum+=quotient;
                }
            }


            div++;
        }
        return 1+sum==x;
    }

}
