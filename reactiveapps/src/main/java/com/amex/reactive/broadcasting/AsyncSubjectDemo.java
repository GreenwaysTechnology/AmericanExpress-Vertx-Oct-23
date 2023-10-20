package com.amex.reactive.broadcasting;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.AsyncSubject;

public class AsyncSubjectDemo {
    public static void main(String[] args) {

        Observable<Integer> sourceStream = Observable.create(subscriber -> {
            for (int i = 0; i <= 20; i++) {
                subscriber.onNext(i);
            }
            subscriber.onComplete();
        });
//        Observable<Integer> sourceStream = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //Create Async Subject
        AsyncSubject<Integer> asyncSubject = AsyncSubject.create();
        //connect source stream with async subject
        sourceStream.subscribe(asyncSubject);

        asyncSubject.subscribe(data -> {
            System.out.println("Subu's AsyncSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });
        asyncSubject.subscribe(data -> {
            System.out.println("Ram's AsyncSubject Subject :" + data);
        }, err -> {
            System.out.println(err);
        }, () -> {
            System.out.println("Done!");
        });
    }
}
