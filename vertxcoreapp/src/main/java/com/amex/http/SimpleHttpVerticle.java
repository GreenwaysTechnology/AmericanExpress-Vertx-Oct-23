package com.amex.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

public class SimpleHttpVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", SimpleHttpVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //Create Server
    HttpServer httpServer = vertx.createHttpServer();

    //Your application code: Request and Response handling
    httpServer.requestHandler(request -> {
      HttpServerResponse response = request.response();
      response.end("Hello");
    });
    //deploy and start server
    httpServer.listen(3000, httpServerHandler -> {
      if (httpServerHandler.succeeded()) {
        System.out.println("Server is Up @" + httpServerHandler.result().actualPort());
      } else {
        System.out.println("Server start up failed " + httpServerHandler.cause().getMessage());
      }
    });

  }
}
