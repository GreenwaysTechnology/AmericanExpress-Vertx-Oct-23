package com.amex.async.timers;

import io.vertx.core.*;

public class TimerWithDataVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", TimerWithDataVerticle.class.getName());
  }

  //Data Transfer with Future with delay
  public Future<String> getMessage() {
    return Future.future(ar -> {
      vertx.setTimer(1000, handler -> {
        String response = "Hello!!!";
        ar.complete(response);
      });
    });
  }

  //Data transfer using callbacks pattern
  public void transfer(Handler<AsyncResult<String>> aHandler) {
    vertx.setTimer(2000, timerHandler -> {
      String response = "Hello,How are you!";
      aHandler.handle(Future.succeededFuture(response));
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println("start");
    getMessage().onSuccess(System.out::println);
    transfer(aResult -> {
      if (aResult.succeeded()) {
        System.out.println(aResult.result());
      }
    });
    System.out.println("end");
  }
}
