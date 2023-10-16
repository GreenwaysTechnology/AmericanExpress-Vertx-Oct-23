package com.amex;

import com.amex.verticles.GreeterVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;

public class LanucherDeployer extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", LanucherDeployer.class.getName());
  }

  @Override
  public void start() throws Exception {
    super.start();
    System.out.println("Deployer starting");
    vertx.deployVerticle(new GreeterVerticle());
  }

  @Override
  public void stop() throws Exception {
    super.stop();
    System.out.println("Deployer ending");
  }
}
