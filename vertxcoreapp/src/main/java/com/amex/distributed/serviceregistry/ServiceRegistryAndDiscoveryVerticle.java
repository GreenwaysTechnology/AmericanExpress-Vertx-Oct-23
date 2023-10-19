package com.amex.distributed.serviceregistry;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.servicediscovery.Record;
import io.vertx.servicediscovery.ServiceDiscovery;
import io.vertx.servicediscovery.ServiceDiscoveryOptions;
import io.vertx.servicediscovery.types.HttpEndpoint;

class GreeterRestVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    Router router = Router.router(vertx);

    router.get("/api/hello").handler(rc -> {
      rc.response().end("Hello,Service Discovery and Registry");
    });
    vertx.createHttpServer().requestHandler(router).listen(3000);
  }
}

//Consumer Verticle , which communicates GreeterRestVerticle via WebClient not directly but via
//Service Registry.
class ConsumerVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //Service Communication via Service Discovery
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    //enable discovery server
    discoveryOptions.setBackendConfiguration(new JsonObject()
      .put("connection", "127.0.0.1:2181")
      .put("ephemeral", true)
      .put("guaranteed", true)
      .put("basePath", "/services/my-backend")
    );
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);

    Router router = Router.router(vertx);

    router.get("/api/greet").handler(rc -> {
      //Communicate Service Registry and get Record,Get Reference.
      HttpEndpoint.getWebClient(discovery, new JsonObject().put("name", "greeterApiRecord"), ar -> {
        //Get Reference from the Record
        WebClient webClient = ar.result();
        //do your job with Resource
        webClient.get("/api/hello").send(result -> {
          System.out.println("Response is ready!");
          rc.response().end(result.result().bodyAsString());
        });
        rc.response().endHandler(ar1 -> {
          //remove /release discovery record
          ServiceDiscovery.releaseServiceObject(discovery, webClient);
        });
      });

    });
    vertx.createHttpServer().requestHandler(router).listen(8080);


  }
}


//Publisher Verticle publishes
class PublisherVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //Service Registry Server configuration
    ServiceDiscoveryOptions discoveryOptions = new ServiceDiscoveryOptions();
    discoveryOptions.setBackendConfiguration(new JsonObject()
      .put("connection", "127.0.0.1:2181")
      .put("ephemeral", true)
      .put("guaranteed", true)
      .put("basePath", "/services/my-backend")
    );
    //Service Registry instance
    ServiceDiscovery discovery = ServiceDiscovery.create(vertx, discoveryOptions);

    //Create Record
    Record record = HttpEndpoint.createRecord("greeterApiRecord", "localhost", 3000, "/api/hello");

    //publish the Record
    //Publish the Record
    discovery.publish(record, ar -> {
      if (ar.succeeded()) {
        System.out.println("Successfully published >>>>" + ar.result().toJson());
      } else {
        System.out.println(" Not Published " + ar.cause());
      }
    });
  }
}

public class ServiceRegistryAndDiscoveryVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", ServiceRegistryAndDiscoveryVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.deployVerticle(new PublisherVerticle());
    vertx.deployVerticle(new GreeterRestVerticle());
    vertx.deployVerticle(new ConsumerVerticle());
  }
}
