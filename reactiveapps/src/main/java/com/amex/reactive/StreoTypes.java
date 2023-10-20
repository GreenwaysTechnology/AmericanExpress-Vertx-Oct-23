package com.amex.reactive;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class StreoTypes {
    public static void main(String[] args) {
          // createSingle();
       // createMayBe();
        createCompleteable();
    }

    private static void createCompleteable() {
        Completable.complete().subscribe(() -> System.out.println("Completeable"));
    }
    private static void createMayBe() {
        //only item
        Maybe.just(1).subscribe(System.out::println);
        //only error
        Maybe.error(new RuntimeException("error")).subscribe(System.out::println, System.out::println);
        //only complete
        Maybe.empty().subscribe(System.out::println, System.out::println, () -> System.out.println("onComplete"));
    }

    private static void createSingle() {
        Single.create(emitter -> {
            emitter.onSuccess("Hello");
            emitter.onSuccess("Hi");
        }).subscribe(System.out::println);

        Single.create(emitter -> {
            emitter.onError(new RuntimeException("error"));
        }).subscribe(System.out::println, System.out::println);

        Single.just(1).subscribe(System.out::println);

//          Single.just(1,2,3).subscribe(System.out::println);

    }
}
