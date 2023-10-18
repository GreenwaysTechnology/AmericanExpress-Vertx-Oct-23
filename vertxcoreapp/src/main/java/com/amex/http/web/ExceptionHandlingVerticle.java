package com.amex.http.web;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class ExceptionHandlingVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", ExceptionHandlingVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router userRouter = Router.router(vertx);
    Router appRouter = Router.router(vertx);
    HttpServer httpServer = vertx.createHttpServer();

    appRouter.route("/api/users/*").subRouter(userRouter);


    userRouter.get("/list/:name").handler(ctx -> {
      String name = ctx.pathParam("name");
      if (name.equals("admin")) {
        ctx.response().end("You are valid user");
      } else {
        throw new RuntimeException("invalid user");
      }
    }).failureHandler(ctx -> {
      int statusCode = ctx.statusCode();
      String message = ctx.failure().getMessage();
      JsonObject errorMessages = new JsonObject().put("code", statusCode).put("message", message);
      ctx.response().setStatusCode(statusCode).end(errorMessages.encodePrettily());
    });
    //assign router to request handler
    httpServer.requestHandler(appRouter);
    //start server
    httpServer.listen(3000, handler -> {
      if (handler.succeeded()) {
        System.out.println("Vertx Server is running!!");
      } else {

      }
    });
  }
}
