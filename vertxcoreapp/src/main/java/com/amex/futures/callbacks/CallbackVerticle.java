package com.amex.futures.callbacks;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;

//Model class
class User {
  private String userName;
  private String password;

  public User() {
  }

  public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "User{" +
      "userName='" + userName + '\'' +
      ", password='" + password + '\'' +
      '}';
  }
}

public class CallbackVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", CallbackVerticle.class.getName());
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

    getUser().onComplete(userHandler -> {
      if (userHandler.succeeded()) {
        // System.out.println(userHandler.result());
        //call login method
        login(userHandler.result()).onComplete(loginHandler -> {
          if (loginHandler.succeeded()) {
            //call show dashboard
            showDashboard(loginHandler.result()).onComplete(dashboardHandler -> {
              if (dashboardHandler.succeeded()) {
                System.out.println(dashboardHandler.result());
              } else {
                System.out.println(dashboardHandler.cause().getMessage());
              }
            });
          } else {
            System.out.println(loginHandler.cause().getMessage());
          }
        });
      } else {
        System.out.println(userHandler.cause().getMessage());
      }
    });
  }
}
