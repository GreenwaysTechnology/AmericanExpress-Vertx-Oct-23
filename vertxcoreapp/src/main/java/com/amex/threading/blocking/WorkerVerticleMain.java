package com.amex.threading.blocking;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

//worker verticle -  worker thread
//inside worker verticle if you write any httpserver which runs in the event loop thread since
//this createHttpServer is non blocking api always runs in event loop thread.
//if you want to write a router with blocking code execution, you have to use router.blockingHandler
class DelayVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println(DelayVerticle.class.getName() + " is running on " + Thread.currentThread().getName());
    try {
      Thread.sleep(10000);
      System.out.println("I am delayed Message");
    } catch (Exception e) {

    }
  }
}

class GreeterVerticle extends AbstractVerticle {

  //run only this code in a separate thread of execution
  private void sayHello(Promise<Void> promise) {
    try {
      System.out.println("sayHello method " + " is running on " + Thread.currentThread().getName());

      Thread.sleep(10000);
      System.out.println("Hello from sayHello");
      promise.complete();
    } catch (Exception e) {
      promise.fail("something went wrong");
    }
  }

  public void findAll(Promise<JsonObject> promise) {
    System.out.println("findAll is running " + Thread.currentThread().getName());
    try {
      Thread.sleep(5000);
      promise.complete(new JsonObject().put("message", "Hello"));
    } catch (Exception es) {
    }
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    System.out.println(GreeterVerticle.class.getName() + " is running on " + Thread.currentThread().getName());
    vertx.executeBlocking(this::sayHello);
    vertx.executeBlocking(this::findAll).onSuccess(res -> {
      System.out.println("Got " + res.encodePrettily());
    });
    //get data from the blocking api
    //router with blocking :
    Router router = Router.router(vertx);
    //
    router.get("/api/blocking").blockingHandler(rc -> {
      System.out.println("findAll is running " + Thread.currentThread().getName());
      try {
        Thread.sleep(5000);
        rc.response().end(new JsonObject().put("message", "Hello").encodePrettily());
      } catch (Exception es) {
      }
    });
    vertx.createHttpServer().requestHandler(router).listen(3000, ar -> {
      if (ar.succeeded()) {
        System.out.println("Server is Running");
      }
    });

    vertx.createHttpServer().requestHandler(router).listen(3000, ar -> {
      if (ar.succeeded()) {
        System.out.println("Server is Running");
      }
    });
  }
}

public class WorkerVerticleMain extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", WorkerVerticleMain.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //worker Verticle
    DeploymentOptions options = new DeploymentOptions().setWorker(true);
    vertx.deployVerticle(new DelayVerticle(), options);
    vertx.deployVerticle(new GreeterVerticle());

  }
}
