package com.example.reactive;

import com.example.functional.OneArgumentExpressionWithException;

/**
 * Created by debasishc on 29/1/17.
 */
public class MapperTask implements Task {
    OneArgumentExpressionWithException mapper;
    Task nextTask;

    public MapperTask(
            OneArgumentExpressionWithException mapper,
            Task nextTask) {
        this.mapper = mapper;
        this.nextTask = nextTask;
    }

}
