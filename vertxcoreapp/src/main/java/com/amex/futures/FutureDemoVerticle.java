package com.amex.futures;

import io.vertx.core.*;

public class FutureDemoVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", FutureDemoVerticle.class.getName());
  }

  //Callee
  //create simple future and return no Result.
  public Future<Void> getEmptyFuture() {
    //create Future Object : Anonymous pattern using java inner class
//    Future<Void> future = Future.future(new Handler<Promise<Void>>() {
//      @Override
//      public void handle(Promise<Void> event) {
//        event.complete();
//      }
//    });
    // create Future Object : using lambda pattern:It is recommended
    Future<Void> future = Future.future(event -> {
      event.complete();
    });
    return future;
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("Future Verticle");
    //caller
//    getEmptyFuture().onComplete(new Handler<AsyncResult<Void>>() {
//      @Override
//      public void handle(AsyncResult<Void> event) {
//        //here only we grab result
//        if (event.succeeded()) {
//          System.out.println("Success");
//          event.result();
//        } else {
//          System.out.println("Failed");
//          System.out.println(event.cause().getMessage());
//        }
//      }
//    });
    getEmptyFuture().onComplete(event -> {
      if (event.succeeded()) {
        System.out.println("Success");
        event.result();
      } else {
        System.out.println("Failed");
        System.out.println(event.cause().getMessage());
      }
    });
  }
}
