package com.amex.reactive.broadcasting;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.BehaviorSubject;

import java.util.concurrent.TimeUnit;

public class HotBehvaiourSubject {
    public static void main(String[] args) throws InterruptedException {
        //soure stream
          Observable<Long> sourceStream = Observable.interval(1000, TimeUnit.MILLISECONDS);

        //This will emit default value
//        Observable sourceStream = Observable.create(observer->{
//
//        });
        //create Behaviour Subject
        BehaviorSubject<Long> behaviorSubject = BehaviorSubject.createDefault(0l);
        //connecting source stream with Behaviour
        sourceStream.subscribe(behaviorSubject);

        System.out.println("Wait for some seconds");
        Thread.sleep(10000);
        // BehaviourSubject emits the most recently item at the time of subscription
        behaviorSubject.subscribe(data -> {
            System.out.println("Subu's BehaviorSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });

        behaviorSubject.subscribe(data -> {
            System.out.println("Ram's BehaviorSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });
        System.out.println("Wait for some seconds");
        Thread.sleep(10000);
    }
}
