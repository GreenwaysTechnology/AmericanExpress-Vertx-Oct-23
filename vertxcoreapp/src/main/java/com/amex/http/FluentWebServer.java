package com.amex.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class FluentWebServer extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", FluentWebServer.class.getName());
  }

  private static void handle(HttpServerRequest request) {
    request.response().end("Hello!");
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
//    vertx.createHttpServer()
//      .requestHandler(request -> request.response().end("Hello"))
//      .listen(3000, handler -> {
//        System.out.println("Vertx Server is Running!");
//      });

    vertx.createHttpServer()
      .requestHandler(FluentWebServer::handle)
      .listen(3000, handler -> {
        System.out.println("Vertx Server is Running!");
      });
  }
}
