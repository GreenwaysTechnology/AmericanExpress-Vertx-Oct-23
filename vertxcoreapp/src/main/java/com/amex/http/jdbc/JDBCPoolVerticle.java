package com.amex.http.jdbc;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.sqlclient.Row;

public class JDBCPoolVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", JDBCPoolVerticle.class.getName());
  }

  public void prepareDb() {
    JsonObject config = new JsonObject()
      .put("url", "jdbc:hsqldb:mem:test?shutdown=true")
      .put("driver_class", "org.hsqldb.jdbcDriver")
      .put("max_pool_size", 30)
      .put("user", "SA")
      .put("password", "");
    //Pool is ready
    JDBCPool pool = JDBCPool.pool(vertx, config);
    //create,insert,select
    pool
      .query("create table test(id int primary key,name varchar(255))")
      .execute()
      .compose(r ->
        pool.query("insert into test values(1,'subramanian'),(2,'murugan')").execute()
      ).compose(r -> pool.query("select * from test").execute())
      .onSuccess(rows -> {
        for (Row row : rows) {
          System.out.println(row.toJson());
        }
      }).onFailure(System.out::println);

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    prepareDb();
  }
}
