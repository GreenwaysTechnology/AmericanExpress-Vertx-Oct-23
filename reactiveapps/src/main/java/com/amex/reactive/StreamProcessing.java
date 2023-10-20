package com.amex.reactive;

import io.reactivex.rxjava3.core.Observable;

public class StreamProcessing {
    public static void main(String[] args) {
        // transform();
        filter();
    }

    private static void filter() {
        Observable
                .range(1, 20)
                .filter(i -> {
                    System.out.println("Item got " + i);
                    return i >= 5;
                })
                .subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

    }

    public static void transform() {
        //Fluent pattern/builder pattern
        Observable.range(1, 20)
                .map(a -> {
                    System.out.println("item emitted 1 -> " + a);
                    return a * 10;
                }).map(b -> {
                    System.out.println("item emitted 2 -> ");
                    return b * 2;
                })
                .subscribe(System.out::println,
                        System.out::println,
                        () -> System.out.println("done"));

    }
}
