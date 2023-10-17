package com.amex.futures.callbacks;

import io.vertx.core.*;

public class CompositFutureVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", CompositFutureVerticle.class.getName());
  }

  //start Db server
  public Future<String> startDbServer() {
    System.out.println("Db Server started");
    return Future.succeededFuture("Db Server is Up");

  }


  //start web server
  public Future<String> startWebServer() {
    System.out.println("Web Server is started");
   // return Future.succeededFuture("WebServer is Up");
    return Future.failedFuture("Failed WebServer startup");
  }

  //config Server
  public Future<String> startConfigServer() {
    System.out.println("Config Server is started");
    return Future.succeededFuture("ConfigServer is Up");
  }

  @Override
  public void start() throws Exception {
    super.start();


    Future<String> dbServer = startDbServer();
    Future<String> webServer = startWebServer();
    Future<String> configServer = startConfigServer();

    CompositeFuture.all(dbServer, webServer, configServer).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("All Server is Up");
        //do something....
      } else {
        System.out.println(ar.cause().getMessage());
      }
    });

    System.out.println("...Any...");
    CompositeFuture.any(dbServer, webServer, configServer).onComplete(ar -> {
      if (ar.succeeded()) {
        System.out.println("All Server is Up");
        //do something....
      } else {
        System.out.println(ar.cause().getMessage());
      }
    });
  }
}
