package com.test.large.response;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.LoggerFormat;
import io.vertx.ext.web.handler.LoggerHandler;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {
  private JsonObject finalJsonObject;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);
    router.route().handler(LoggerHandler.create(LoggerFormat.SHORT));
    router.get("/large").handler(this::largeResponseHandler);
    finalJsonObject = new JsonObject(vertx.fileSystem().readFileBlocking("test.json"));
    vertx.createHttpServer(new HttpServerOptions().setCompressionSupported(true)).requestHandler(router).listen(8080, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }


  private void largeResponseHandler(RoutingContext routingContext) {
    JsonObject jsonObject = new JsonObject(finalJsonObject.toBuffer());
    jsonObject.remove("index");
    jsonObject.remove("eyeColor");
    routingContext.response().setChunked(true).putHeader("uuid", UUID.randomUUID().toString()).end(jsonObject.toBuffer());
  }
}
