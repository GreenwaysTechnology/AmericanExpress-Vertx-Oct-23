package com.amex;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;

import javax.crypto.spec.PSource;

public class FileSystemVerticle extends AbstractVerticle {
  public static void main(String[] args) {
    Launcher.executeCommand("run", FileSystemVerticle.class.getName());
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    super.start(startPromise);
    vertx.fileSystem()
      .exists("src/resources/info.txt")
      .compose(exits -> {
        if (exits) {
          return vertx.fileSystem().delete("src/resources/info.txt");
        } else {
          return vertx.fileSystem().createFile("src/resources/info.txt");
        }
      }).compose(file -> {
        return vertx.fileSystem().writeFile("src/resources/info.txt", Buffer.buffer("hello"));
      }).onSuccess(res -> {
        System.out.println("File write operation is completed");
      }).onFailure(err -> {
        System.out.println(err.getMessage());
      });
  }
}
