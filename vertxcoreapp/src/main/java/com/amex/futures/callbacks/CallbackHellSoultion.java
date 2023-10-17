package com.amex.futures.callbacks;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Future;

public class CallbackHellSoultion extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", CallbackHellSoultion.class.getName());
  }

  //getUser api
  private Future<User> getUser() {
    System.out.println("Get User is called");
    User user = new User("admin", "admin");
//    user = null;
    if (user != null) {
      return Future.succeededFuture(user);
    } else {
      return Future.failedFuture("User Not Found");
    }
  }

  //login api
  private Future<String> login(User user) {
    System.out.println("Login  is called");
    if (user.getUserName().equals("admin") && user.getPassword().equals("admin")) {
      return Future.succeededFuture("login success");
    } else {
      return Future.failedFuture("Login failed ");
    }
  }

  //show dashboard
  private Future<String> showDashboard(String status) {
    System.out.println("showDashboard  is called");
    if (status.equals("login success")) {
      return Future.succeededFuture("You are admin");
    } else {
      return Future.failedFuture("You are not admin");
    }
  }

  @Override
  public void start() throws Exception {
    super.start();
    //solution 1:
//    getUser().onSuccess(user -> {
//      login(user).onSuccess(status -> {
//        showDashboard(status).onSuccess(page -> {
//          System.out.println(page);
//        }).onFailure(err -> System.out.println(err));
//      }).onFailure(loginErr -> {
//        System.out.println(loginErr.getMessage());
//      });
//    }).onFailure(userErr -> {
//      System.out.println(userErr.getMessage());
//    });

//    //solution 2: Using Future.compose method
//    getUser().compose(user -> {
//      return login(user);
//    }).compose(status -> {
//      return showDashboard(status);
//    }).onSuccess(res -> {
//      System.out.println(res);
//    }).onFailure(err -> {
//      System.out.println(err.getMessage());
//    });
    //Solution 2.1: Using Future.compose method lambda refactoring.

//    getUser()
//      .compose(user -> login(user))
//      .compose(status -> showDashboard(status))
//      .onSuccess(res -> {
//        System.out.println(res);
//      }).onFailure(err -> {
//        System.out.println(err.getMessage());
//      });
    //Soution 2.2 : Using Future.compose using Method reference
    getUser()
      .compose(this::login)
      .compose(this::showDashboard)
      .onSuccess(System.out::println)
      .onFailure(System.out::println);
  }
}
