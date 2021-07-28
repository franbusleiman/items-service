FROM openjdk:8
VOLUME /tmp
ADD ./target/items-0.0.1-SNAPSHOT.jar items-service.jar
ENTRYPOINT ["java", "-jar", "/items-service.jar"]