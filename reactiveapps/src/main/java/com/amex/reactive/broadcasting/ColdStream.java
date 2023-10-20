package com.amex.reactive.broadcasting;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.TimeUnit;

public class ColdStream {
    public static void main(String[] args) throws InterruptedException {
        //
        Observable<Long> myObservable = Observable.interval(1, TimeUnit.SECONDS);

        myObservable.subscribe(item -> System.out.println("Observer 1: " + item));
        //after 3scs new subscriber joins
        Thread.sleep(3000);
        myObservable
                .doOnSubscribe((r) -> System.out.println("Observer 2 Joining"))
                .subscribe(item -> System.out.println("Observer 2: " + item));
        Thread.sleep(5000);

    }
}
