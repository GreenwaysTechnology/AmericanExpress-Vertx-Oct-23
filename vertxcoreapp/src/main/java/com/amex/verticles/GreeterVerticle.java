package com.amex.verticles;

import io.vertx.core.AbstractVerticle;

public class GreeterVerticle extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("GreeterVerticle");
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    System.out.println("GreeterVerticle has been stopped");
  }
}
