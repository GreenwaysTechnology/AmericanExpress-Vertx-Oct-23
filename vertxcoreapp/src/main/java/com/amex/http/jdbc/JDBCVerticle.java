package com.amex.http.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;
import jdk.jshell.ImportSnippet;

public class JDBCVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", JDBCVerticle.class.getName());
  }

  public void prepareDatabase() {
    JsonObject connectionInfo = new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver")
      .put("max_pool_size", 30)
      .put("user", "SA")
      .put("password", "");

    JDBCClient jdbcClient = JDBCClient.createShared(vertx, connectionInfo);
    jdbcClient.getConnection(connection -> {
      if (connection.succeeded()) {
        System.out.println("connection success");
        SQLConnection sqlConnection = connection.result();
        //table creation
        String CREATE_TABLE = "CREATE TABLE user(id int primary key, name varchar(255))";
        sqlConnection.execute(CREATE_TABLE, tableCreate -> {
          if (tableCreate.succeeded()) {
            System.out.println("table is created");
            String INSERT_QUERY = "INSERT INTO user VALUES(1,'subramanian')";
            sqlConnection.execute(INSERT_QUERY, tableInsert -> {
              if (tableInsert.failed()) {
                System.out.println(tableInsert.cause().getMessage());
              } else {
                String SELECT_QUERY = "SELECT * FROM user";
                sqlConnection.query(SELECT_QUERY, tableSelect -> {
                  System.out.println(tableSelect.result().getResults());
                });
              }
            });
          } else {
            System.out.println("Table not created" + tableCreate.cause().getMessage());
          }
        });
      } else {
        System.out.println("Connection failed" + connection.cause().getMessage());
      }
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    prepareDatabase();
  }
}
