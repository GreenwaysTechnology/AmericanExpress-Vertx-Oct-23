package com.amex;

import io.vertx.core.*;
import io.vertx.core.impl.ConcurrentHashSet;
import io.vertx.core.json.JsonObject;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.EventBusService;
import io.vertx.servicediscovery.types.HttpEndpoint;
import io.vertx.servicediscovery.types.MessageSource;
import io.vertx.servicediscovery.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MicroServiceVerticle extends AbstractVerticle {

  protected ServiceDiscovery discovery;
  protected Set<Record> registeredRecords = new ConcurrentHashSet<>();

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    discovery = ServiceDiscovery.create(vertx, new ServiceDiscoveryOptions().setBackendConfiguration(config()));

  }

  public void publishHttpEndpoint(String name, String host, int port, Handler<AsyncResult<Void>>
    completionHandler) {
    Record record = HttpEndpoint.createRecord(name, host, port, "/");
    publish(record, completionHandler);
  }

  public void publishMessageSource(String name, String address, Class<?> contentClass, Handler<AsyncResult<Void>>
    completionHandler) {
    Record record = MessageSource.createRecord(name, address, contentClass);
    publish(record, completionHandler);
  }

  public void publishMessageSource(String name, String address, Handler<AsyncResult<Void>>
    completionHandler) {
    Record record = MessageSource.createRecord(name, address);
    publish(record, completionHandler);
  }

  public void publishEventBusService(String name, String address, Class<?> serviceClass, Handler<AsyncResult<Void>>
    completionHandler) {
    Record record = EventBusService.createRecord(name, address, serviceClass);
    publish(record, completionHandler);
  }

  protected void publish(Record record, Handler<AsyncResult<Void>> completionHandler) {
    if (discovery == null) {
      try {
        start();
      } catch (Exception e) {
        throw new RuntimeException("Cannot create discovery service");
      }
    }

    discovery.publish(record, ar -> {
      if (ar.succeeded()) {
        registeredRecords.add(record);
      }
      completionHandler.handle(ar.map((Void) null));
    });
  }


  @Override
  public void stop(Promise<Void> future) throws Exception {
    List<Promise> futures = new ArrayList<>();
    for (Record record : registeredRecords) {
      Promise<Void> unregistrationFuture = Promise.promise();
      futures.add(unregistrationFuture);
      discovery.unpublish(record.getRegistration(), unregistrationFuture);
    }

    if (futures.isEmpty()) {
      discovery.close();
      future.complete();
    } else {
      future.fail("failed");
    }
  }
}
