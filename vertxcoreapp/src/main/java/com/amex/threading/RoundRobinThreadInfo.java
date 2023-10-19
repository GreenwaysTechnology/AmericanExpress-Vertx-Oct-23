package com.amex.threading;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;

class HelloVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println(HelloVerticle.class.getName() + " is  Running on " + Thread.currentThread().getName());
  }
}

public class RoundRobinThreadInfo extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", RoundRobinThreadInfo.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println("Current Thread + " + Thread.currentThread().getName());
    for (int i = 0; i < 27; i++) {
      vertx.deployVerticle(new HelloVerticle());
    }
  }
}
