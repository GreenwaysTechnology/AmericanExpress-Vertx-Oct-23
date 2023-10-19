package com.amex.http.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Row;

import java.util.HashMap;
import java.util.Map;

public class RestAPIWithDatabase extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", RestAPIWithDatabase.class.getName());
  }

  JsonObject config = new JsonObject()
    .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
    .put("driver_class", "org.hsqldb.jdbcDriver")
    .put("max_pool_size", 30)
    .put("user", "SA")
    .put("password", "");

  JDBCPool pool;

  private void handleListProducts(RoutingContext routingContext) {
    JsonArray arr = new JsonArray();
    pool.query("select * from products")
      .execute()
      .onSuccess(rows -> {
        for (Row row : rows) {
          System.out.println("row = " + row.toJson());
          arr.add(row.toJson());
        }
        routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());

      }).onFailure(Throwable::printStackTrace);
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

  private void setUpInitialData() {
    pool.query("create table products(id int primary key, name varchar(255))")
      .execute()
      .compose(r ->
        // insert some test data
        pool
          .query("insert into products values (1, 'phone'), (2, 'food')")
          .execute()
      ).compose(r ->
        // query some data
        pool
          .query("select * from products")
          .execute()
      ).onSuccess(rows -> {
        for (Row row : rows) {
          System.out.println("row = " + row.toJson());
        }
      }).onFailure(Throwable::printStackTrace);
  }

  private void handleAddProduct(RoutingContext routingContext) {
    HttpServerResponse response = routingContext.response();
    JsonObject product = routingContext.body().asJsonObject();
    if (product == null) {
      sendError(400, response);
    } else {
      String INSERT_QUERY = "insert into products values (" + product.getInteger("id") + ",'" + product.getString("name") + "')";
      pool.query(INSERT_QUERY).execute().onSuccess(res -> {
        response.setStatusCode(201).end("saved");
      }).onFailure(err -> {
        response.end(err.getMessage());
      });

    }

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    pool = JDBCPool.pool(vertx, config);
    setUpInitialData();
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.post("/products").handler(this::handleAddProduct);
    router.get("/products").handler(this::handleListProducts);
    vertx.createHttpServer().requestHandler(router).listen(8080);

  }

}
