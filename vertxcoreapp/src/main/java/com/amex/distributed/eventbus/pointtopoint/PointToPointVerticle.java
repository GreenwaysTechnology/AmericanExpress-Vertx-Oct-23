package com.amex.distributed.eventbus.pointtopoint;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;


class Address {
  public static String POINT_TO_POINT = "covid.fin.request";
}

//publisher
class FinanceRequestVerticle extends AbstractVerticle {
  public void requestFinance() {
    System.out.println("Finance Request started....");
    vertx.setTimer(5000, ar -> {
      //point to point : send method
      String message = "Dear Team, We request that we want 1 Billion $  for Covid recovery fund";
      //point to point ; send
      vertx.eventBus().send(Address.POINT_TO_POINT, message);
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    requestFinance();
  }
}

//consumer

class CenertalFinanceVerticle extends AbstractVerticle {

  public void consume() {
    EventBus eventBus = vertx.eventBus();
    MessageConsumer<String> messageConsumer = eventBus.consumer(Address.POINT_TO_POINT);
    //handle /process the message/news
    messageConsumer.handler(news -> {
      System.out.println("Request   : " + news.body());
    });
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start();
    consume();
  }
}


public class PointToPointVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", PointToPointVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new FinanceRequestVerticle());
    vertx.deployVerticle(new CenertalFinanceVerticle());

  }
}
