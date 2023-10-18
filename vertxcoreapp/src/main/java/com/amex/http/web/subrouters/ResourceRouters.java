package com.amex.http.web.subrouters;

import com.amex.http.web.PathParameter;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ResourceRouters extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", ResourceRouters.class.getName());
  }

  //router class : inner classes
  private static class UserRouter {
    //CURD operations
    public static void create(RoutingContext ctx) {
      JsonObject user = ctx.body().asJsonObject();
      System.out.println(user.encodePrettily());
      ctx.response().setStatusCode(201).end("User saved");
    }

    public static void list(RoutingContext ctx) {
      ctx.response().setStatusCode(200).end("User List");
    }
  }

  private static class ProductRouter {
    //CURD operations
    public static void create(RoutingContext ctx) {
      JsonObject user = ctx.body().asJsonObject();
      System.out.println(user.encodePrettily());
      ctx.response().setStatusCode(201).end("Product saved");
    }

    public static void list(RoutingContext ctx) {
      ctx.response().setStatusCode(200).end("Product List");
    }
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    //Create Http server
    HttpServer httpServer = vertx.createHttpServer();
    //Request handling logic will be inside router
    //create sub routers
    Router userRouter = Router.router(vertx);
    Router productRouter = Router.router(vertx);
    Router customerRouter = Router.router(vertx);
    //appRouter
    Router appRouter = Router.router(vertx);
    //initialize the middleware
    //here we apply middleware for all router
    appRouter.route().handler(BodyHandler.create());


    //URL Patterns
    //http://localhost:3000/api/users/list
    userRouter.get("/list").handler(UserRouter::list);
    userRouter.post("/save").handler(UserRouter::create);

    //Products
    productRouter.get("/list").handler(ProductRouter::list);
    productRouter.post("/save").handler(ProductRouter::create);


    //appRouter with subRouter binding
    //old api: depricated api
//     appRouter.mountSubRouter("/api/users", userRouter);
    //new api
    appRouter.route("/api/users/*").subRouter(userRouter);
    //appRouter.mountSubRouter("/api/products", productRouter);
    appRouter.route("/api/products/*").subRouter(productRouter);

    //assign router to request handler
    httpServer.requestHandler(appRouter);

    //start server
    httpServer.listen(3000, handler -> {
      if (handler.succeeded()) {
        System.out.println("Vertx Server is running!!");
      } else {

      }
    });

  }
}

