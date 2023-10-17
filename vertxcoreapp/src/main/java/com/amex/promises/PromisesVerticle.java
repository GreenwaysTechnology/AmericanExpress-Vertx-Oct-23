package com.amex.promises;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;

public class PromisesVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", PromisesVerticle.class.getName());
  }

  //create Promise and encapulte the data and return.
  public Promise<String> getHelloPromise() {
    Promise promise = Promise.promise();
    promise.complete("Hello");
    return promise;

  }

  public Future<String> getHaiPromise() {
    Promise promise = Promise.promise();
    promise.complete("Hai");
    return promise.future();

  }

  @Override
  public void start() throws Exception {
    super.start();
    getHelloPromise().future().onSuccess(System.out::println);
    getHaiPromise().onSuccess(System.out::println);
  }
}
