package com.amex.promises;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class GreeterVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println("Greeter Verticle");
  }
}
