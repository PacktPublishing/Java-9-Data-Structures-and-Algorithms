package com.example.reactive;

import com.example.functional.OneArgumentStatement;
import com.example.functional.OneArgumentStatementWithException;

/**
 * Created by debasishc on 29/1/17.
 */
public class ProcessorTask<E> implements Task{
    OneArgumentStatementWithException<E> processor;

    public ProcessorTask(
            OneArgumentStatementWithException<E> processor) {
        this.processor = processor;
    }
}
