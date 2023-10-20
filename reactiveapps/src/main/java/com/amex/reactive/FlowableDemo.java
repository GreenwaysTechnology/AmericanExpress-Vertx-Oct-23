package com.amex.reactive;

import io.reactivex.rxjava3.core.Flowable;

public class FlowableDemo {
    public static void main(String[] args) {
        createFlowable();
    }

    private static void createFlowable() {
        //push + pull - Reactive Stream
        Flowable.range(1, 100).subscribe(System.out::println);
    }
}
