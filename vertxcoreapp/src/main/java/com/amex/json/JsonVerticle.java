package com.amex.json;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class JsonVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", JsonVerticle.class.getName());
  }

  public Future<JsonObject> getUser() {
    Promise promise = Promise.promise();
//    JsonObject user = new JsonObject();
//    user.put("id", 1);
//    user.put("name", "Subramanian");
//    user.put("city", "Coimbatore");
//    promise.complete(user);

    JsonObject skills = new JsonObject()
      .put("skillId", 1)
      .put("skill", new JsonArray()
        .add("Java")
        .add("Vertx")
        .add("Spring Boot")
        .add("Quarkus")
      );

    //fluent way
    JsonObject user = new JsonObject()
      .put("id", 1)
      .put("name", "Subramanian")
      .put("city", "Coimbatore")
      .mergeIn(skills)
      .put("company", new JsonObject().put("name", "American Express"));

    promise.complete(user);

    return promise.future();
  }

  public Future<JsonArray> getUsers() {
    Promise<JsonArray> promise = Promise.promise();
    JsonArray users = new JsonArray()
      .add(new JsonObject()
        .put("id", 1)
        .put("name", "Subramanian")
        .put("city", "Coimbatore")
        .put("company", new JsonObject().put("name", "American Express"))).add(
        new JsonObject()
          .put("id", 2)
          .put("name", "Murugan")
          .put("city", "Coimbatore")
          .put("company", new JsonObject().put("name", "American Express"))
      );
    promise.complete(users);

    return promise.future();
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    getUser().onSuccess(user -> {
      System.out.println("Id " + user.getInteger("id"));
      System.out.println("Name " + user.getString("name"));
      System.out.println("City " + user.getString("city"));
      System.out.println(user.encodePrettily());
    });
    getUsers().onSuccess(users -> {
      System.out.println(users.encodePrettily());
    });
  }
}
