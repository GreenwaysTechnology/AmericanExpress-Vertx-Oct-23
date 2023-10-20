package com.amex.distributed.scalling;

import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.http.HttpServer;

public class GreeterService extends AbstractVerticle {
  @Override
  public void start() throws Exception {
    super.start();
    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestStream().toFlowable().subscribe(req -> {
      req.response().end("Hello!How are you : " + Thread.currentThread().getName() + " =>" + hashCode());
    });
    httpServer.rxListen(3000).subscribe(onSuccess -> {
      System.out.println("Server is Running on " + onSuccess.actualPort());
    });
  }
}
