package com.amex.distributed.webclient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;

class HelloVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());

    router.get("/api/hello").handler(ctx -> {
      ctx.response().end("Hello");
    });
    vertx.createHttpServer().requestHandler(router).listen(3000, handler -> {
      if (handler.succeeded()) {
        System.out.println("Provider Server is running");
      }
    });

  }
}

class GreeterVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    WebClient webClient = WebClient.create(vertx);
    router.get("/api/greet").handler(ctx -> {
      //communicate with HelloRest verticle
      HttpRequest<Buffer> httpRequest = webClient.get(3000, "localhost", "/api/hello");
      httpRequest.send().onSuccess(response -> {
        ctx.response().end(response.body().toString());
      });
    });
    router.get("/api/posts").handler(ctx -> {
      HttpRequest<Buffer> httpRequest = webClient.
        getAbs("https://jsonplaceholder.typicode.com/posts");
      httpRequest.send().onSuccess(posts -> {
        ctx.response()
          .setStatusCode(200)
          .putHeader("content-type", "application/json")
          .end(posts.bodyAsJsonArray().encodePrettily());
      }).onFailure(err -> {
      });
    });
    vertx.createHttpServer().requestHandler(router).listen(3001, handler -> {
      if (handler.succeeded()) {
        System.out.println("Consumer Server is running");
      }
    });

  }
}

public class WebClientVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", WebClientVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new HelloVerticle());
    vertx.deployVerticle(new GreeterVerticle());
  }
}
