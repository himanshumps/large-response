FROM registry.access.redhat.com/ubi8/openjdk-17@sha256:79585ca02551ecff9d368905d7ce387232b9fd328256e7a715ae3c4ec7b086d3
COPY ./target/large-response-1.0.0-SNAPSHOT-fat.jar ./app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]
