package com.test.large.response;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.impl.cpu.CpuCoreSensor;

public class Application {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class,
      new DeploymentOptions()
        .setWorker(false)
        .setInstances(CpuCoreSensor.availableProcessors() * 2)
      , event -> {
        if (event.succeeded()) {
          System.out.println("MainVerticle Deployed");
        }
        else {
          event.cause().printStackTrace();
        }
      });
  }
}
