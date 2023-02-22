package com.test.large.response;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {
  private JsonObject finalJsonObject;

  private String responseStr;

  @Override
  public void start(Promise<Void> startPromise) {
    Router router = Router.router(vertx);
    //router.route().handler(LoggerHandler.create(LoggerFormat.SHORT));
    router.get("/jsonProcessing").handler(this::jsonProcessing);
    router.get("/noProcessing").handler(this::noProcessing);
    finalJsonObject = new JsonObject(vertx.fileSystem().readFileBlocking("test.json"));
    responseStr = vertx.fileSystem().readFileBlocking("test.json").toString();
    vertx.createHttpServer(new HttpServerOptions().setCompressionSupported(true)).requestHandler(router).listen(8080, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8080");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private void noProcessing(RoutingContext routingContext) {
    routingContext.response().putHeader("uuid", UUID.randomUUID().toString()).end(responseStr);
  }

  private void jsonProcessing(RoutingContext routingContext) {
    JsonObject jsonObject = new JsonObject(responseStr);
    jsonObject.remove("index");
    jsonObject.remove("eyeColor");
    routingContext.response().setChunked(true).putHeader("uuid", UUID.randomUUID().toString()).end(jsonObject.toBuffer());
  }
}
