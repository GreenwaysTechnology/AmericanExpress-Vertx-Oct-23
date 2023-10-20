package com.amex.reactive;

import io.reactivex.rxjava3.core.Single;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.rxjava3.core.AbstractVerticle;
import io.vertx.rxjava3.core.eventbus.EventBus;
import io.vertx.rxjava3.core.http.HttpServer;
import io.vertx.rxjava3.ext.web.client.HttpResponse;
import io.vertx.rxjava3.ext.web.client.WebClient;
import io.vertx.rxjava3.ext.web.codec.BodyCodec;

class ReactiveWebClient extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    vertx.createHttpServer().requestHandler(req -> {
      WebClient webClient = WebClient.create(vertx);
      Single<HttpResponse<String>> request = webClient
        .get(3000, "localhost", "/")
        .as(BodyCodec.string()).rxSend();
      //fire the http call
      request.subscribe(result -> {
        req.response().end(result.body());
      });
    }).rxListen(3001).subscribe();
  }
}

class EventBusReactive extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    EventBus eventBus = vertx.eventBus();

    eventBus.consumer("com.amex.addr").toFlowable().subscribe(message -> {
      System.out.println(message.body().toString());
      message.reply("PONG");
    });
    vertx.setPeriodic(1000, v -> {
      eventBus.rxRequest("com.amex.addr", "PING").subscribe(reply -> {
        System.out.println("Received Reply " + reply.body());
      });
    });
  }
}


class WebServerVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);

    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestStream().toFlowable().subscribe(req -> {
      req.response().end("Stream data over http using web client");
    });
    httpServer.rxListen(3000).subscribe(server -> {
      System.out.println("Server is running");
    });
  }
}

class GreeterVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);

  }
}

public class ReactiveMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", ReactiveMainVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    vertx.deployVerticle(new GreeterVerticle()).subscribe(res -> {
      System.out.println(res);
    }, err -> {
      System.out.println(err);
    });
    vertx.deployVerticle(new WebServerVerticle()).subscribe(res -> {
      System.out.println(res);
    }, err -> {
      System.out.println(err);
    });
    vertx.deployVerticle(new EventBusReactive()).subscribe(res -> {
      System.out.println(res);
    }, err -> {
      System.out.println(err);
    });
    vertx.deployVerticle(new ReactiveWebClient()).subscribe(res -> {
      System.out.println(res);
    }, err -> {
      System.out.println(err);
    });
  }
}
