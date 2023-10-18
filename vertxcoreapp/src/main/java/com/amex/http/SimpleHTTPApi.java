package com.amex.http;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;

public class SimpleHTTPApi extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", SimpleHTTPApi.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.createHttpServer().requestHandler(request -> {
      //building rest api -  url and method
      if (request.uri().equals("/api/users") && request.method() == HttpMethod.GET) {
        request.response().setStatusCode(200).end("USERS Get");
      }
      if (request.uri().equals("/api/users") && request.method() == HttpMethod.POST) {
        request.response().setStatusCode(200).end("USERS Post");
      }
      if (request.uri().equals("/api/users") && request.method() == HttpMethod.PUT) {
        request.response().setStatusCode(200).end("USERS Put");
      }
      if (request.uri().equals("/api/users") && request.method() == HttpMethod.DELETE) {
        request.response().setStatusCode(200).end("USERS DELETE");
      }
    }).listen(3000, handler -> {
      if (handler.succeeded()) {
        System.out.println("Vertx Server is Running");
      } else {
        System.out.println("Server start up failed");
      }
    });
  }
}
