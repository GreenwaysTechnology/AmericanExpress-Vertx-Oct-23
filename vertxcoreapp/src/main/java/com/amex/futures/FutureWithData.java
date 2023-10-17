package com.amex.futures;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;

public class FutureWithData extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", FutureWithData.class.getName());
  }

  //Return data with Future.
  public Future<String> sayHello() {
    return Future.future(future -> future.complete("Hello"));
  }

  //return error data with Future
  public Future<String> getError() {
    return Future.future(future -> future.fail("Something went wrong!"));
  }

  //future with biz logic
  public Future<String> login(String userName, String password) {

    return Future.future(handler -> {
      //biz logic
      if (userName.equals("admin") && password.equals("admin")) {
        handler.complete("Login Success");
      } else {
        handler.fail("Login failed");
      }
    });

  }


  @Override
  public void start() throws Exception {
    super.start();
    //subscribe future .
    sayHello().onComplete(event -> {
      if (event.succeeded()) {
        System.out.println(event.result());
      } else {
        System.out.println(event.cause());
      }
    });
    //grab error Message
    getError().onComplete(event -> {
      if (event.failed()) {
        System.out.println(event.cause().getMessage());
      }
    });
    //login with success and error
    login("admin", "admin").onComplete(event -> {
      if (event.failed()) {
        System.out.println(event.cause().getMessage());
      } else {
        System.out.println(event.result());
      }
    });
    //login with failure data
    login("foo", "bar").onComplete(event -> {
      if (event.failed()) {
        System.out.println(event.cause().getMessage());
      } else {
        System.out.println(event.result());
      }
    });
    //short cut apis
    //sayHello().onSuccess(res -> System.out.println(res));
    sayHello().onSuccess(System.out::println);
    getError().onFailure(System.out::println);
    login("admin", "admin")
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
  }
}
