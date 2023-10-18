package com.amex.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class RequestPayload extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", RequestPayload.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //Create Http server
    HttpServer httpServer = vertx.createHttpServer();

    //Request handling logic will be inside router
    Router router = Router.router(vertx);

    //initialize the middleware
    //here we apply middleware for all router
    router.route().handler(BodyHandler.create());

    //Resources: USER resource
    router.get("/api/users").handler(ctx -> {
      ctx.response().setStatusCode(200).end("USERS -GET");
    });

    router.post("/api/users").handler(ctx -> {
      //current vertx deprecated this api
      //JsonObject payload = ctx.getBodyAsJson();
      //lastest vertx api body
      JsonObject payload = ctx.body().asJsonObject();
      System.out.println(payload);
      ctx.response().setStatusCode(201).end("USERS -POST");

    });
    router.put("/api/users").handler(ctx -> {
      ctx.response().setStatusCode(200).end("USERS -PUT");

    });
    router.delete("/api/users").handler(ctx -> {
      ctx.response().setStatusCode(200).end("USERS -DELETE");
    });
    //assign router to request handler
    httpServer.requestHandler(router);

    //start server
    httpServer.listen(3000, handler -> {
      if (handler.succeeded()) {
        System.out.println("Vertx Server is running!!");
      }
    });

  }
}

