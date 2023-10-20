package com.amex.distributed.scalling.clustering;


import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.spi.cluster.ClusterManager;
import io.vertx.spi.cluster.hazelcast.HazelcastClusterManager;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class ConsumerVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    ClusterManager mgr = new HazelcastClusterManager();
    VertxOptions options = new VertxOptions().setClusterManager(mgr).setHAEnabled(true).setHAGroup("g1");
    Vertx.clusteredVertx(options, cluster -> {
      if (cluster.succeeded()) {
        //Get the reference of Cluster Powered Vertx Engine
        Vertx vertx = cluster.result();
        //deploy the verticle in cluster.
        DeploymentOptions deploymentOptions = new DeploymentOptions().setInstances(3);
        cluster.result().deployVerticle("com.amex.distributed.scalling.clustering.ConsumerVerticle", deploymentOptions).onSuccess(res -> {
          System.out.println("Deployment Id => " + res);
        }).onFailure(err -> {
          System.out.println(err);
        });
      } else {
        // failed!
        System.out.println("Cluster is failed" + cluster.cause());
      }
    });


  }


  @Override
  public void start(Promise<Void> startFuture) throws Exception {
    super.start(startFuture);
    System.out.println("started");
    EventBus eventBus = (EventBus) vertx.eventBus();
    //Declare Consumer
    MessageConsumer<String> consumer = eventBus.consumer("news.in.covid");
    //handle/process the message/news
    consumer.handler(news -> {
      RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
      String jvmName = runtimeBean.getName();
      System.out.println("Node  Name = " + jvmName);
      long pid = Long.valueOf(jvmName.split("@")[0]);
      System.out.println("PID  = " + pid + " Thread = " + Thread.currentThread().getName());
      System.out.println("News 7's Today News : " + news.body());
    });
  }
}
