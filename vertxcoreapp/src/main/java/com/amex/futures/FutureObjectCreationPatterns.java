package com.amex.futures;

import io.vertx.core.*;

public class FutureObjectCreationPatterns extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", FutureObjectCreationPatterns.class.getName());
  }

  //Pattern 1:
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

  //Pattern 2:
  public Future<String> auth(String userName, String password) {
    if (userName.equals("admin") && password.equals("admin")) {
      return Future.succeededFuture("Login Success");
    }
    return Future.failedFuture("Login Failed");
  }

  //Pattern 3: Function as parameter : Higher order function
  public void authenticat(String userName, String password, Handler<AsyncResult<String>> asyncHandler) {
    if (userName.equals("admin") && password.equals("admin")) {
      asyncHandler.handle(Future.succeededFuture("Login success"));
    } else {
      asyncHandler.handle(Future.failedFuture("Login Failed"));
    }
  }

  @Override
  public void start() throws Exception {
    super.start();
    //pattern 1:
    login("admin", "admin")
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
    // pattern 2:
    auth("admin", "admin")
      .onSuccess(System.out::println)
      .onFailure(System.out::println);

    //Pattern 3:
    authenticat("admin", "admin", handler -> {
      if (handler.failed()) {
        System.out.println(handler.cause().getMessage());
      } else {
        System.out.println(handler.result());
      }
    });
  }
}
