package com.amex;

import com.amex.verticles.GreeterVerticle;
import io.vertx.core.Vertx;

public class VertlcleDeployer {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new GreeterVerticle());
  }
}
