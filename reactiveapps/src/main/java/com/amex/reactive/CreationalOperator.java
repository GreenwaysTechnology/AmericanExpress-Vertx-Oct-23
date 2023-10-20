package com.amex.reactive;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.List;

public class CreationalOperator {
    public static void main(String[] args) {
//                Observable<Integer> intStream =Observable.create(observer->{
////            observer.onNext(1);
////            observer.onNext(2);
//              observer.onComplete()
////        });
        Observable<Integer> intStream = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        intStream.subscribe(System.out::println, System.out::println,
                () -> System.out.println("done"));

        Integer[] items = {1, 2, 3, 4, 5, 6};
        Observable<Integer> stream = Observable.fromArray(items);
        stream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

        Integer[] itemsList = {1, 2, 3, 4, 5, 6};
        List<Integer> list = Arrays.asList(itemsList);
        Observable<Integer> listStream = Observable.fromIterable(list);
        listStream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

        Observable<Integer> rangeStream = Observable.range(1, 1000);
        rangeStream.subscribe(System.out::println, System.out::println, () -> System.out.println("done"));

    }
}
