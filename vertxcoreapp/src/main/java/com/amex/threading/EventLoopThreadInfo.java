package com.amex.threading;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;

public class EventLoopThreadInfo extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", EventLoopThreadInfo.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println("Current Thread + " + Thread.currentThread().getName());
  }
}
