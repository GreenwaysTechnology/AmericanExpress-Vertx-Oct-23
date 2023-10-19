package com.amex.distributed.eventbus.pubsub;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

//Address
class Address {
  public static String PUB_SUB_ADDRESS = "news.in.covid";
}
//Publisher Verticle

//publisher publish news to all news tv channels
class PublisherVerticle extends AbstractVerticle {
//  EventBus eventBus = vertx.eventBus();

  public void publish() {
    //Get EventBus Object reference
    EventBus eventBus = vertx.eventBus();
    String message = "Last 24 Hrs covid count is 80000";
    //send message with timer
//    vertx.setTimer(1000, ar -> {
//      eventBus.publish(Address.PUB_SUB_ADDRESS, message);
//    });
    HttpServer httpServer = vertx.createHttpServer();

    //Request handling logic will be inside router
    Router router = Router.router(vertx);

    //route handler: Request handler
    router.route().handler(ctx -> {
      HttpServerResponse response = ctx.response();
      response.setStatusCode(200);
      eventBus.publish(Address.PUB_SUB_ADDRESS, message);
      response.end(message + "has been published");
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

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    publish();
  }
}

//subscribers
class NewsSevenVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(news -> {
      System.out.println(this.getClass().getName() + news.body());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    consume();
  }
}

class BBCVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(news -> {
      System.out.println(this.getClass().getName() + news.body());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    consume();
  }
}

class NDTVVerticle extends AbstractVerticle {
  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.PUB_SUB_ADDRESS);
    messageConsumer.handler(news -> {
      System.out.println(this.getClass().getName() + news.body());
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    consume();
  }
}


//Subscriber verticles

public class PubSubVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", PubSubVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new PublisherVerticle());
    vertx.deployVerticle(new NewsSevenVerticle());
    vertx.deployVerticle(new BBCVerticle());
    vertx.deployVerticle(new NDTVVerticle());
  }
}
