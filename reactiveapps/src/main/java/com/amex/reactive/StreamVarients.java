package com.amex.reactive;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class StreamVarients {
    public static void main(String[] args) throws InterruptedException {
        //        I dont want to send data,or error,only complete signal
//        empty
        Observable stream = Observable.empty();
        stream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));
        Observable<String> errorStream = Observable.error(new RuntimeException("Something went wrong"));
        errorStream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

        //infnite
        Observable<Long> clock = Observable.interval(1000, TimeUnit.MILLISECONDS);
        clock.subscribe(System.out::println, System.out::println,
                () -> System.out.println("done"));
        Thread.sleep(Long.MAX_VALUE);
    }
}
