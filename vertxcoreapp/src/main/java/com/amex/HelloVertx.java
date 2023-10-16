package com.amex;

import io.vertx.core.Vertx;

public class HelloVertx {
  public static void main(String[] args) {
    System.out.println("Creating vertx app");
    Vertx vertx = Vertx.vertx();
    System.out.println(vertx.getClass());

    System.out.println("Main thread" + Thread.currentThread().getName());
    //create simple web server
    vertx.createHttpServer().requestHandler(req -> {
      System.out.println("Server");
      System.out.println("Vertx thread : " + Thread.currentThread().getName());
      req.response().end("Hello Vertx");
    }).listen(8080);

  }
}
