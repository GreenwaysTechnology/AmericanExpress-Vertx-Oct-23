package com.amex.threading.blocking;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;

class HelloVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //Thread sleep is going to block thread
    System.out.println("Hello Verticle is Running  " + Thread.currentThread().getName());
    Thread.sleep(10000);
    System.out.println("Hello, I am delayed Message");
  }
}

public class BlockingVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", BlockingVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println("Main Thread + " + Thread.currentThread().getName());
    vertx.deployVerticle(new HelloVerticle());

  }
}
