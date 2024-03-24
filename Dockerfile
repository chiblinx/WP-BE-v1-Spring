FROM ubuntu:latest
LABEL authors="Lekan Adetunmbi"

WORKDIR /app

COPY . .

RUN gradle clean build -x test

FROM container-registry.oracle.com/graalvm/jdk:21

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

CMD ["java", "-jar", "app.jar"]