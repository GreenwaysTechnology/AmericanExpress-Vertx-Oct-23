package com.amex.threading;

import io.vertx.core.Vertx;

public class App {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.createHttpServer().requestHandler(req -> {
      System.out.println(Thread.currentThread().getName());
      req.response().end("hello");
    }).listen(3000, ar -> {

    });
    vertx.setTimer(300,ar->{
      System.out.println(Thread.currentThread().getName());
    });
  }
}
