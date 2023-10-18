package com.amex.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class SimpleWebAPI extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", SimpleWebAPI.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //Create Http server
    HttpServer httpServer = vertx.createHttpServer();

    //Request handling logic will be inside router
    Router router = Router.router(vertx);

    //route handler: Request handler
    router.route().handler(ctx -> {
      HttpServerResponse response = ctx.response();
      response.setStatusCode(200);
      response.putHeader("content-type", "application/json");
      JsonObject jsonObject = new JsonObject().put("message", "Hello Vertx");
      response.end(jsonObject.encodePrettily());
    });
    //assign router to request handler
    httpServer.requestHandler(router);

    //start server
    httpServer.listen(3000, handler -> {
      if (handler.succeeded()) {
        System.out.println("Vertx Server is running!");
      }
    });

  }
}
