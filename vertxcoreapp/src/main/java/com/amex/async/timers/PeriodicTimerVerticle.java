package com.amex.async.timers;

import io.vertx.core.*;

public class PeriodicTimerVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", PeriodicTimerVerticle.class.getName());
  }


  public void tick() {
    long timerId = vertx.setPeriodic(1000, handler -> {
      System.out.println(Math.random());
    });
    //cancel the timer after certain time or certain condition
    vertx.setTimer(10000, handler -> {
      System.out.println("Stop Polling");
      vertx.cancelTimer(timerId);
    });
  }

  //send data to the caller
  public void poll(Handler<AsyncResult<Double>> aHandler) {
    long timerId = vertx.setPeriodic(1000, handler -> {
      aHandler.handle(Future.succeededFuture(Math.random()));
    });
    //cancel the timer after certain time or certain condition
    vertx.setTimer(10000, handler -> {
      System.out.println("Stop Polling");
      vertx.cancelTimer(timerId);
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //tick();
//    poll(ar -> {
//      if (ar.succeeded()) {
//        System.out.println(ar.result());
//      }
//    });
    vertx.createHttpServer()
      .requestHandler(req -> req.response().end("Ok"))
      .listen(8080, ar -> {
        if (ar.succeeded()) {
          startPromise.complete();
        } else {
          startPromise.fail(ar.cause());
        }
      });
  }
}
