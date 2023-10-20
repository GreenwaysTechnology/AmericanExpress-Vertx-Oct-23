package com.amex.reactive;

import io.reactivex.rxjava3.core.Observable;

import java.util.Date;

public class FirstStream {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(observer -> {
            observer.onNext("hello");
            observer.onNext("hai");
            Thread.sleep(10000);
            observer.onNext("welcome");
            observer.onComplete();
        });
        //Listen for event
        observable.subscribe(data -> {
            System.out.println(data + new Date());
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Completed");
        });
    }
}
