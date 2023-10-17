package com.amex.promises;

import com.amex.verticles.GreeterVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;

public class PromiseStartVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", PromiseStartVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new GreeterVerticle(), ar -> {
      if (ar.succeeded()) {
        System.out.println(GreeterVerticle.class.getName() + " Deployement Id  " + ar.result());
        startPromise.future().onSuccess(System.out::println); //ensure that this verticle has been successfull
      } else {
        //startPromise.fail("Something went wrong");
      }
    });
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    super.stop(stopPromise);
  }
}
