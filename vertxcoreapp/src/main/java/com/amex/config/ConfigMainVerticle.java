package com.amex.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

class GreeterVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //get config data
    JsonObject config = config();
    String message = config.getString("message", "default");
    int port = config.getInteger("port", 8080);
    vertx.createHttpServer().requestHandler(re -> {
      re.response().end(message);
    }).listen(port);

  }
}

class ConfigFileRetriverVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);

    //Store Configuration
    ConfigStoreOptions fileStoreOptions = new ConfigStoreOptions();
    fileStoreOptions.setType("file");
    fileStoreOptions.setFormat("json");
    fileStoreOptions.setConfig(new JsonObject().put("path", "conf/config.json"));

    //Retriver Otions
    ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions().addStore(fileStoreOptions);

    //ConfigRetriver
    ConfigRetriever retriever = ConfigRetriever.create(vertx, configRetrieverOptions);

    retriever.getConfig().onSuccess(config -> {
      System.out.println(config.encodePrettily());
      vertx.createHttpServer().requestHandler(res -> {
        res.response().end(config.encodePrettily());
      }).listen(config.getInteger("port", 3003));
    }).onFailure(err -> {
      System.out.println(err.getMessage());
    });
  }
}



public class ConfigMainVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", ConfigMainVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //config object
    JsonObject config = new JsonObject().put("message", "Hello Config").put("port", 3000);
    DeploymentOptions options = new DeploymentOptions().setConfig(config);
    vertx.deployVerticle(new GreeterVerticle(), options);
    vertx.deployVerticle(new ConfigFileRetriverVerticle());
//    vertx.deployVerticle(new ConfigFromGitVerticle());
  }
}
