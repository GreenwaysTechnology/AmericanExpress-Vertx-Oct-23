package com.amex;

import com.amex.verticles.GreeterVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Context;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {
  //override verticle life cycle methods
  @Override
  public void init(Vertx vertx, Context context) {
    super.init(vertx, context);
    System.out.println("Init is called");
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("Start method is called");
    //deploy Greeter Verticle
    //vertx variable is already available within every verticle we create.
    // vertx.deployVerticle(new GreeterVerticle());
    //vertx.deployVerticle(GreeterVerticle.class.getName());
    vertx.deployVerticle("com.amex.verticles.GreeterVerticle");

  }

  @Override
  public void stop() throws Exception {
    super.stop();
    System.out.println("stop method is called");
    vertx.undeploy("com.amex.verticles.GreeterVerticle");
  }
}
