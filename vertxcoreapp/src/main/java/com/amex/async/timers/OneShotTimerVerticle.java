package com.amex.async.timers;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;

public class OneShotTimerVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", OneShotTimerVerticle.class.getName());
  }
  //simple delay
  public void delay() {
    vertx.setTimer(1000, handler -> {
      System.out.println("I am delayed task");
    });
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println("start");
    delay();
    System.out.println("end");
  }
}
