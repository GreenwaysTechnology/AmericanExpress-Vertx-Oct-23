package com.amex.distributed.scalling;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.rxjava3.core.AbstractVerticle;

public class ScallerDeployer extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", ScallerDeployer.class.getName());
  }

  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    DeploymentOptions options = new DeploymentOptions().setInstances(5);
    vertx.rxDeployVerticle(GreeterService.class.getName(), options).subscribe(res -> {
      System.out.println(GreeterService.class.getName() + " " + res);
    }, err -> {
      System.out.println(err);
    });
  }
}


